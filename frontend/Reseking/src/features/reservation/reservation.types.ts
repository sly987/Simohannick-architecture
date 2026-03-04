export type VehicleType = "electrique" | "essence";

export type Reservation = {
  id?: number;
  startDate: string;
  duration: number;
  vehicleType: VehicleType;
};