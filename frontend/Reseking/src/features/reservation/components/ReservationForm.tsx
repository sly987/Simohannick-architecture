import { useState } from "react";
import DatePicker from "react-datepicker";
import { fr } from "date-fns/locale";
import type { Reservation, VehicleType } from "../reservation.types";
import "../Reservation.css";
type Props = {
  onSubmit: (reservation: Reservation) => void;
};

export function ReservationForm({ onSubmit }: Props) {
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [duration, setDuration] = useState(1);
  const [vehicleType, setVehicleType] =
    useState<VehicleType>("essence");

  const handleSubmit = () => {
    if (!startDate) return;

    onSubmit({
      startDate: startDate.toISOString().split("T")[0],
      duration,
      vehicleType,
    });
  };

  return (

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
          onChange={(e) => setVehicleType(e.target.value as VehicleType)}
          className="input-field"
        >
          <option value="electrique">Électrique</option>
          <option value="essence">Essence</option>
        </select>
      </div>
      <button onClick={handleSubmit}>
        Réserver ma place
      </button>
    </div>

  );
}