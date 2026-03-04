export type Role = "EMPLOYEE" | "MANAGER" | "ADMIN";

export type User = {
  email: string;
  firstName: string;
  lastName: string;
  role: Role;
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
