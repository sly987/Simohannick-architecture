import { useState, useEffect } from "react";
import Modal from "../../shared/components/modal";
import { ReservationForm } from "./components/ReservationForm";
import { useReservation } from "./useReservation";
import type { Reservation } from "./reservation.types";
import type { ParkingSpot } from "../parking/parking.types";
import ParkingGrid from "../parking/components/ParkingGrid";
import { fetchParkingSpots } from "../parking/parking.api";
export default function ReservationPage() {
  const { createReservation } = useReservation();
  const [selectedSpot, setSelectedSpot] = useState<ParkingSpot | null>(null);
  const [isOpen, setIsOpen] = useState(false);
  const [spots, setSpots] = useState<ParkingSpot[]>([]);

useEffect(() => {
    async function fetchSpots() {
      try {
        const data = await fetchParkingSpots();
        setSpots(data);
      } catch (error) {
        console.error("Erreur lors de la récupération des parkings", error);
      }
    }
    fetchSpots();
  }, []);
  const handleSpotClick = (spot: ParkingSpot) => {
    setSelectedSpot(spot);
    setIsOpen(true);
  };

  const handleCreate = async (reservation: Reservation) => {
    await createReservation(reservation);
    setIsOpen(false); // ferme la modal après succès
  };

  return (
    <div className="reservation-layout">
      <ParkingGrid spots={spots} onSelectSpot={handleSpotClick} />


      <Modal isOpen={isOpen} onClose={() => setIsOpen(false)}>
        {selectedSpot && (
          <ReservationForm
            selectedSpot={selectedSpot}
            onSubmit={handleCreate}
          />
        )}
      </Modal>

    </div>
  );
}