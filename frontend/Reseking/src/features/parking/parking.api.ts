import type { ParkingSpot } from "./parking.types";

export async function fetchParkingSpots(): Promise<ParkingSpot[]> {
  const response = await fetch("http://localhost:8080/api/spots");
  return response.json();
}