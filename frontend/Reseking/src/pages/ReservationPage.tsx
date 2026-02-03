import { useState } from "react";
import "../App.css";
import DatePicker from "react-datepicker";
import "react-datepicker/dist/react-datepicker.css";
import { fr } from "date-fns/locale";

export default function ReservationPage() {
    const [startDate, setStartDate] = useState<Date | null>(null);
    const [duration, setDuration] = useState<number>(1);
    const [vehicleType, setVehicleType] = useState<string>("essence");
    const handleReservation = async () => {
        if (!startDate || duration <= 0) {
            alert("Veuillez remplir tous les champs");
            return;
        }

        const payload = {
            startDate: startDate.toISOString().split("T")[0], // yyyy-MM-dd
            duration,
            vehicleType,
        };

        try {
            const response = await fetch(
                "http://localhost:8080/api/reservations",
                {
                    method: "POST",
                    headers: { "Content-Type": "application/json" },
                    body: JSON.stringify(payload),
                }
            );

            if (!response.ok) {
                throw new Error(`Erreur : ${response.status}`);
            }

            const message = await response.text(); // ou .json() si ton backend renvoie JSON
            alert(`Succès : ${message}`);
        } catch (error) {
            console.error(error);
            alert(`Erreur lors de la réservation : ${(error as Error).message}`);
        }
    };

    return (
        <div className="reservation-container">
            <div className="reservation-card">
                <h1>🚗 Réservation de parking</h1>
                <p className="subtitle">
                    Choisissez votre créneau de stationnement
                </p>

                <div className="field">
                    <label>Date de début</label>
                    <DatePicker
                        selected={startDate}
                        onChange={(date: Date | null) => setStartDate(date)}
                        dateFormat="dd-MM-yyyy"
                        placeholderText="jj-mm-aaaa"
                        locale={fr}
                    />
                </div>

                <div className="field">
                    <label>Durée (en jours)</label>
                    <input
                        type="number"
                        min={1}
                        value={duration}
                        onChange={(e) => setDuration(Number(e.target.value))}
                        className="input-field"
                    />
                </div>
                <div className="field">
                    <label>Type de véhicule</label>
                    <select
                        value={vehicleType}
                        onChange={(e) => setVehicleType(e.target.value)}
                        className="input-field"
                    >
                        <option value="electrique">Électrique</option>
                        <option value="essence">Essence</option>
                    </select>
                </div>
                <button onClick={handleReservation}>
                    Réserver ma place
                </button>
            </div>
        </div>
    );
}
