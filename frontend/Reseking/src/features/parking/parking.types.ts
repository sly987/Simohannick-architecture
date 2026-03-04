export type ParkingSpot = {
  id: number;
  row: string;
  column: number;
  type: "ELECTRIC" | "STANDARD";
  activeReservation: number | null;
};