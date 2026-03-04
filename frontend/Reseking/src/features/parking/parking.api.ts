import type { ParkingSpot } from "./parking.types";
import { tokenStorage } from "../auth/auth.api";
const BASE_URL = `${import.meta.env.VITE_API_URL}/spots`;
const getAuthHeaders = (): HeadersInit => {
  const token = tokenStorage.get();
  return {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
  };
};
export async function fetchParkingSpots(): Promise<ParkingSpot[]> {
   const response = await fetch(BASE_URL, {
      headers: getAuthHeaders(),
    });
    if (!response.ok) throw new Error("Erreur chargement");
    return response.json();
}