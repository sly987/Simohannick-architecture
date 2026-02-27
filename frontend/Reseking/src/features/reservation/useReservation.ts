import { useEffect, useState } from "react";
import { reservationApi } from "./reservation.api";
import type { Reservation } from "./reservation.types";

export function useReservation() {
  const [reservations, setReservations] = useState<Reservation[]>([]);
  const [loading, setLoading] = useState(false);

  const loadReservations = async () => {
    try {
      setLoading(true);
      const data = await reservationApi.findAll();
      setReservations(data);
    } finally {
      setLoading(false);
    }
  };

  const createReservation = async (reservation: Reservation) => {
    await reservationApi.create(reservation);
    await loadReservations();
  };

  useEffect(() => {
    loadReservations();
  }, []);

  return {
    reservations,
    loading,
    createReservation,
  };
}