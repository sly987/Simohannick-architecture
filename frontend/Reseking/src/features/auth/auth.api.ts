import type { AuthResponse, LoginCredentials, User } from "./auth.types";

const BASE_URL = `${import.meta.env.VITE_API_URL}/auth`;
const MOCK_ENABLED = import.meta.env.VITE_MOCK_AUTH === "true";

const MOCK_USERS: Record<string, User> = {
  "admin@reseking.fr": {
    id: 1,
    email: "admin@reseking.fr",
    firstName: "Admin",
    lastName: "Reseking",
    role: "ADMIN",
  },
  "manager@reseking.fr": {
    id: 2,
    email: "manager@reseking.fr",
    firstName: "Marie",
    lastName: "Manager",
    role: "MANAGER",
  },
  "employee@reseking.fr": {
    id: 3,
    email: "employee@reseking.fr",
    firstName: "Jean",
    lastName: "Dupont",
    role: "EMPLOYEE",
  },
};

const mockLogin = async (credentials: LoginCredentials): Promise<AuthResponse> => {
  const user = MOCK_USERS[credentials.email];

  if (!user || credentials.password !== "password") {
    throw new Error("Identifiants invalides");
  }

  return { token: `mock-token-${user.id}`, user };
};

const mockMe = async (token: string): Promise<User> => {
  const userId = token.replace("mock-token-", "");
  const user = Object.values(MOCK_USERS).find((u) => u.id === Number(userId));

  if (!user) {
    throw new Error("Session invalide");
  }

  return user;
};

export const authApi = {
  async login(credentials: LoginCredentials): Promise<AuthResponse> {
    if (MOCK_ENABLED) {
      return mockLogin(credentials);
    }

    const response = await fetch(`${BASE_URL}/login`, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify(credentials),
    });

    if (!response.ok) {
      throw new Error("Identifiants invalides");
    }

    return response.json();
  },

  async me(token: string): Promise<User> {
    if (MOCK_ENABLED) {
      return mockMe(token);
    }

    const response = await fetch(`${BASE_URL}/me`, {
      headers: { Authorization: `Bearer ${token}` },
    });

    if (!response.ok) {
      throw new Error("Session invalide");
    }

    return response.json();
  },
};

export const tokenStorage = {
  get: (): string | null => localStorage.getItem("token"),
  set: (token: string): void => localStorage.setItem("token", token),
  remove: (): void => localStorage.removeItem("token"),
};
