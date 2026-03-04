import type { LoginCredentials, LoginResponse, User } from "./auth.types";

const BASE_URL = `${import.meta.env.VITE_API_URL}/auth`;

export const authApi = {
  async login(credentials: LoginCredentials): Promise<LoginResponse> {
    try {
      const response = await fetch(`${BASE_URL}/login`, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(credentials),
      });

      if (!response.ok) {
        throw new Error("Identifiants invalides");
      }

      return response.json();
    } catch (error) {
      if (error instanceof Error && error.message === "Identifiants invalides") {
        throw error;
      }
      throw new Error("Identifiants invalides");
    }
  },

  async me(token: string): Promise<User> {
    try {
      const response = await fetch(`${BASE_URL}/me`, {
        headers: { Authorization: `Bearer ${token}` },
      });

      if (!response.ok) {
        throw new Error("Session invalide");
      }

      return response.json();
    } catch (error) {
      if (error instanceof Error && error.message === "Session invalide") {
        throw error;
      }
      throw new Error("Session invalide");
    }
  },
};

export const tokenStorage = {
  get: (): string | null => localStorage.getItem("token"),
  set: (token: string): void => localStorage.setItem("token", token),
  remove: (): void => localStorage.removeItem("token"),
};
