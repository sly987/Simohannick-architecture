export type Role = "EMPLOYEE" | "MANAGER" | "ADMIN";

export type User = {
  id: number;
  email: string;
  firstName: string;
  lastName: string;
  role: Role;
};

export type LoginCredentials = {
  email: string;
  password: string;
};

export type AuthResponse = {
  token: string;
  user: User;
};

export type AuthState = {
  user: User | null;
  token: string | null;
  isAuthenticated: boolean;
  isLoading: boolean;
};
