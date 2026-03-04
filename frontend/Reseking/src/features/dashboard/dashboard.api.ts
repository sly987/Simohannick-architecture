import { tokenStorage } from "../auth/auth.api";
import type { DashboardStats, DateRange } from "./dashboard.types";

const BASE_URL = `${import.meta.env.VITE_API_URL}/manager/dashboard`;

const getAuthHeaders = (): HeadersInit => {
  const token = tokenStorage.get();
  return {
    "Content-Type": "application/json",
    ...(token && { Authorization: `Bearer ${token}` }),
  };
};

export const dashboardApi = {
  async getStats(dateRange: DateRange): Promise<DashboardStats> {
    const params = new URLSearchParams({
      from: dateRange.from,
      to: dateRange.to,
    });

    const response = await fetch(`${BASE_URL}?${params}`, {
      headers: getAuthHeaders(),
    });

    if (!response.ok) {
      throw new Error("Erreur lors du chargement des statistiques");
    }

    return response.json();
  },
};
