import { useEffect, useState } from "react";
import { useAuth } from "../auth/useAuth";
import type { ReservationStatus } from "./reservation.types";
import { fetchParkingSpots } from "../parking/parking.api";
import type { ParkingSpot } from "../parking/parking.types";
import "./history.css";

const STATUS_LABELS: Record<ReservationStatus, string> = {
  BOOKED: "Réservé",
  CHECKED_IN: "Enregistré",
  FORFEITED: "Forfait",
  CANCELLED: "Annulé",
  COMPLETED: "Terminé",
};

const STATUS_COLORS: Record<ReservationStatus, string> = {
  BOOKED: "#3498db",
  CHECKED_IN: "#27ae60",
  FORFEITED: "#e74c3c",
  CANCELLED: "#f39c12",
  COMPLETED: "#95a5a6",
};

export function HistoryPage() {
  const { user } = useAuth();
  const reservations = user?.reservations || [];
  const [spots, setSpots] = useState<Map<number, ParkingSpot>>(new Map());

  useEffect(() => {
    fetchParkingSpots()
      .then((data) => {
        const spotMap = new Map<number, ParkingSpot>();
        data.forEach((s) => spotMap.set(s.id, s));
        setSpots(spotMap);
      })
      .catch((err) => console.error("Erreur chargement places:", err));
  }, []);

  const getSpotLabel = (spotId: number) => {
    const spot = spots.get(spotId);
    return spot ? `${spot.row}${spot.column}` : `#${spotId}`;
  };

  return (
    <div className="history">
      <h2>Mes réservations</h2>

      {reservations.length === 0 ? (
        <p className="history-empty">Aucune réservation trouvée.</p>
      ) : (
        <div className="history-list">
          {reservations.map((reservation) => (
            <div key={reservation.id} className="history-card">
              <div className="history-card-header">
                <span className="history-spot">Place {getSpotLabel(reservation.spotId)}</span>
                <span
                  className="history-status"
                  style={{ backgroundColor: STATUS_COLORS[reservation.status!] }}
                >
                  {STATUS_LABELS[reservation.status!]}
                </span>
              </div>
              <div className="history-card-body">
                <div className="history-dates">
                  <span>Du {reservation.startDate}</span>
                  <span>Au {reservation.endDate}</span>
                </div>
                <div className="history-registration">
                  {reservation.registrationNumber}
                </div>
              </div>
            </div>
          ))}
        </div>
      )}
    </div>
  );
}
