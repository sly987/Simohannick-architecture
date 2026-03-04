export type ReservationStatus = "BOOKED" | "CHECKED_IN" | "FORFEITED" | "CANCELLED" | "COMPLETED";

export type Reservation = {
  id?: number;
  startDate: string;
  endDate: string;
  registrationNumber: string;
  employeeId?: number;
  spotId: number;
  status?: ReservationStatus;
};