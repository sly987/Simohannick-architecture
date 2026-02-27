import type { Reservation } from "./reservation.types";

const BASE_URL = "http://localhost:8080/api/reservations";

export const reservationApi = {
  async findAll(): Promise<Reservation[]> {
    const response = await fetch(BASE_URL);
    if (!response.ok) throw new Error("Erreur chargement");
    return response.json();
  },

  async create(reservation: Reservation): Promise<void> {
    const response = await fetch(BASE_URL, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(reservation),
    });

    if (!response.ok) {
      throw new Error("Erreur création réservation");
    }
  },
};