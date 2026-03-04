import { tokenStorage } from "../auth/auth.api";
import type { Reservation } from "../reservation/reservation.types";

const BASE_URL = `${import.meta.env.VITE_API_URL}/secretary`;

const getAuthHeaders = (): HeadersInit => {
  const token = tokenStorage.get();
  return {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
  };
};

export type Employee = {
  id: number;
  firstName: string;
  lastName: string;
  email: string;
  role: string;
};

export type ParkingSpot = {
  id: number;
  row: string;
  column: string;
  type: string;
};

export const secretaryApi = {
  async getAllReservations(): Promise<Reservation[]> {
    const response = await fetch(
      `${import.meta.env.VITE_API_URL}/reservations`,
      { headers: getAuthHeaders() }
    );
    if (!response.ok) throw new Error("Erreur chargement réservations");
    return response.json();
  },

  async getAllEmployees(): Promise<Employee[]> {
    const response = await fetch(`${BASE_URL}/employees`, {
      headers: getAuthHeaders(),
    });
    if (!response.ok) throw new Error("Erreur chargement employés");
    return response.json();
  },

  async getAllSpots(): Promise<ParkingSpot[]> {
    const response = await fetch(
      `${import.meta.env.VITE_API_URL}/spots`,
      { headers: getAuthHeaders() }
    );
    if (!response.ok) throw new Error("Erreur chargement places");
    return response.json();
  },

  async cancelReservation(id: number): Promise<void> {
    const response = await fetch(`${BASE_URL}/reservations/cancel?id=${id}`, {
      method: "PUT",
      headers: getAuthHeaders(),
    });
    if (!response.ok) throw new Error("Erreur annulation réservation");
  },

  async deleteReservation(id: number): Promise<void> {
    const response = await fetch(`${BASE_URL}/reservations?id=${id}`, {
      method: "DELETE",
      headers: getAuthHeaders(),
    });
    if (!response.ok) throw new Error("Erreur suppression réservation");
  },

  async addEmployee(employee: {
    firstName: string;
    lastName: string;
    email: string;
    password: string;
    role: string;
  }): Promise<void> {
    const response = await fetch(`${BASE_URL}/employees`, {
      method: "POST",
      headers: getAuthHeaders(),
      body: JSON.stringify(employee),
    });
    if (!response.ok) throw new Error("Erreur création employé");
  },

  };
