import type { Reservation } from "./reservation.types";
import { tokenStorage } from "../auth/auth.api";

const BASE_URL = `${import.meta.env.VITE_API_URL}/reservations`;

const getAuthHeaders = (): HeadersInit => {
  const token = tokenStorage.get();
  return {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
  };
};

export const reservationApi = {
  async findAll(): Promise<Reservation[]> {
    const response = await fetch(BASE_URL, {
      headers: getAuthHeaders(),
    });
    if (!response.ok) throw new Error("Erreur chargement");
    return response.json();
  },

  async create(reservation: Reservation): Promise<void> {
    const response = await fetch(BASE_URL, {
      method: "POST",
      headers: getAuthHeaders(),
      body: JSON.stringify(reservation),
    });

    if (!response.ok) {
      throw new Error("Erreur création réservation");
    }
  },
};