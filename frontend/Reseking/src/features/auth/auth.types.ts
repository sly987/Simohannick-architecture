import type { Reservation } from "../reservation/reservation.types";

export type Role = "EMPLOYEE" | "MANAGER" | "ADMIN";

export type User = {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: Role;
  reservations?: Reservation[];
};

export type LoginCredentials = {
  email: string;
  password: string;
};

export type LoginResponse = {
  token: string;
  role: string;
};

export type AuthState = {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
};
