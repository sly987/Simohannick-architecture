import { useState } from "react";
import Modal from "../../shared/components/modal";
import { ReservationForm } from "./components/ReservationForm";
import { useReservation } from "./useReservation";
import type { Reservation } from "./reservation.types";

export default function ReservationPage() {
  const {createReservation } = useReservation();
  const [isOpen, setIsOpen] = useState(false);

  const handleCreate = async (reservation: Reservation) => {
    await createReservation(reservation);
    setIsOpen(false); // ferme la modal après succès
  };

  return (
    <div className="reservation-layout">

      <div>
        <button onClick={() => setIsOpen(true)}>
          ➕ Nouvelle réservation
        </button>

      </div>

      <Modal isOpen={isOpen} onClose={() => setIsOpen(false)}>
        <ReservationForm onSubmit={handleCreate} />
      </Modal>

    </div>
  );
}