import { useState } from "react";
import DatePicker from "react-datepicker";
import { fr } from "date-fns/locale";
import { addDays } from "date-fns";
import type { Reservation } from "../reservation.types";
import "../Reservation.css";
import type { ParkingSpot } from "../../parking/parking.types";
import { useAuth } from "../../auth/useAuth";
import "react-datepicker/dist/react-datepicker.css";

type Props = {
  selectedSpot: ParkingSpot;
  onSubmit: (data: Reservation) => void;
};

export function ReservationForm({ selectedSpot, onSubmit }: Props) {
  const { user } = useAuth();
  const [startDate, setStartDate] = useState<Date | null>(null);
  const [endDate, setEndDate] = useState<Date | null>(null);
  const [registrationNumber, setRegistrationNumber] = useState("");

  // Managers can book up to 30 days, employees up to 5 days
  const maxDays = user?.role === "MANAGER" || user?.role === "ADMIN" ? 30 : 5;
  const maxEndDate = startDate ? addDays(startDate, maxDays) : undefined;

  const handleSubmit = () => {
    if (!startDate || !endDate || !selectedSpot) {
      alert("Veuillez sélectionner les dates et une place");
      return;
    }
    if (!registrationNumber.trim()) {
      alert("Veuillez entrer le numéro d'immatriculation");
      return;
    }
    if (endDate < startDate) {
      alert("La date de fin doit être après la date de début");
      return;
    }

    onSubmit({
      startDate: startDate.toISOString().split("T")[0],
      endDate: endDate.toISOString().split("T")[0],
      spotId: selectedSpot.id,
      registrationNumber,
      employeeId: user!.id,
    });
  };

  return (
    <div className="reservation-card">
      <h1>🚗 Réservation de parking</h1>
      {selectedSpot && (
        <p className="selected-spot">
          Place sélectionnée : <strong>{selectedSpot.row}{selectedSpot.column}</strong>
          {selectedSpot.type === "ELECTRIC" && (
            <span className="spot-electric"> (Borne électrique)</span>
          )}
        </p>
      )}
      <p className="subtitle">Choisissez votre créneau de stationnement</p>

      <div className="field">
        <label>Date de début</label>
        <DatePicker
          selected={startDate}
          onChange={(date: Date | null) => setStartDate(date)}
          dateFormat="dd-MM-yyyy"
          placeholderText="jj-mm-aaaa"
          locale={fr}
          filterDate={(date) => date.getDay() !== 0 && date.getDay() !== 6}
        />
      </div>

      <div className="field">
        <label>Date de fin (max {maxDays} jours)</label>
        <DatePicker
          selected={endDate}
          onChange={(date: Date | null) => setEndDate(date)}
          dateFormat="dd-MM-yyyy"
          placeholderText="jj-mm-aaaa"
          locale={fr}
          minDate={startDate || undefined}
          maxDate={maxEndDate}
          filterDate={(date) => date.getDay() !== 0 && date.getDay() !== 6}
        />
      </div>

      <div className="field">
        <label>Numéro d’immatriculation</label>
        <input
          type="text"
          value={registrationNumber}
          onChange={(e) => setRegistrationNumber(e.target.value)}
          className="input-field"
          placeholder="Ex: AB-123-CD"
        />
      </div>

      <button onClick={handleSubmit}>Réserver ma place</button>
    </div>
  );
}