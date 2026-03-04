import type { ParkingSpot } from "./parking.types";
import { tokenStorage } from "../auth/auth.api";

export async function fetchParkingSpots(): Promise<ParkingSpot[]> {
  const token = tokenStorage.get();
  const response = await fetch(`${import.meta.env.VITE_API_URL}/spots`, {
    headers: {
      ...(token && { Authorization: `Bearer ${token}` }),
    },
  });
  if (!response.ok) throw new Error("Erreur chargement places");
  return response.json();
}