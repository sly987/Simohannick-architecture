import { useState } from "react";
import { useParams, useNavigate } from "react-router-dom";
import { tokenStorage } from "../auth/auth.api";
import "./checkin.css";

const BASE_URL = `${import.meta.env.VITE_API_URL}/spots`;

export function CheckInPage() {
  const { row, column } = useParams<{ row: string; column: string }>();
  const navigate = useNavigate();

  const [manualRow, setManualRow] = useState(row || "");
  const [manualColumn, setManualColumn] = useState(column || "");
  const [loading, setLoading] = useState(false);
  const [message, setMessage] = useState<{ type: "success" | "error"; text: string } | null>(null);

  const handleCheckIn = async (r: string, c: string) => {
    if (!r || !c) {
      setMessage({ type: "error", text: "Veuillez entrer la rangée et le numéro" });
      return;
    }

    setLoading(true);
    setMessage(null);

    try {
      const token = tokenStorage.get();
      const response = await fetch(`${BASE_URL}/check-in/${r}/${c}`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
          ...(token && { Authorization: `Bearer ${token}` }),
        },
      });

      if (!response.ok) {
        const error = await response.json();
        throw new Error(error.message || "Erreur check-in");
      }

      setMessage({ type: "success", text: `Check-in réussi pour la place ${r}${c}` });
      setTimeout(() => navigate("/history"), 2000);
    } catch (err) {
      setMessage({ type: "error", text: err instanceof Error ? err.message : "Erreur check-in" });
    } finally {
      setLoading(false);
    }
  };

  // Auto check-in if coming from QR code URL
  const hasUrlParams = row && column;

  return (
    <div className="checkin">
      <h2>Check-in Parking</h2>

      {hasUrlParams ? (
        <div className="checkin-auto">
          <p>Place détectée : <strong>{row}{column}</strong></p>
          <button
            onClick={() => handleCheckIn(row, column)}
            disabled={loading}
            className="checkin-btn"
          >
            {loading ? "Check-in en cours..." : "Confirmer mon arrivée"}
          </button>
        </div>
      ) : (
        <div className="checkin-manual">
          <p>Entrez les informations de votre place :</p>
          <div className="checkin-form">
            <select
              value={manualRow}
              onChange={(e) => setManualRow(e.target.value)}
            >
              <option value="">Rangée</option>
              {["A", "B", "C", "D", "E", "F"].map((r) => (
                <option key={r} value={r}>{r}</option>
              ))}
            </select>
            <select
              value={manualColumn}
              onChange={(e) => setManualColumn(e.target.value)}
            >
              <option value="">Numéro</option>
              {Array.from({ length: 10 }, (_, i) => String(i + 1).padStart(2, "0")).map((c) => (
                <option key={c} value={c}>{c}</option>
              ))}
            </select>
            <button
              onClick={() => handleCheckIn(manualRow, manualColumn)}
              disabled={loading}
              className="checkin-btn"
            >
              {loading ? "..." : "Check-in"}
            </button>
          </div>
        </div>
      )}

      {message && (
        <div className={`checkin-message ${message.type}`}>
          {message.text}
        </div>
      )}
    </div>
  );
}
