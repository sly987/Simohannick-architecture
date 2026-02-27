import { createContext, useEffect, useState, type ReactNode } from "react";
import { authApi, tokenStorage } from "./auth.api";
import type { LoginCredentials, User } from "./auth.types";

type AuthContextType = {
  user: User | null;
  isAuthenticated: boolean;
  isLoading: boolean;
  login: (credentials: LoginCredentials) => Promise<void>;
  logout: () => void;
};

export const AuthContext = createContext<AuthContextType | null>(null);

type AuthProviderProps = {
  children: ReactNode;
};

export function AuthProvider({ children }: AuthProviderProps) {
  const [user, setUser] = useState<User | null>(null);
  const [isLoading, setIsLoading] = useState(true);

  useEffect(() => {
    const initAuth = async () => {
      const token = tokenStorage.get();
      if (!token) {
        setIsLoading(false);
        return;
      }

      try {
        const currentUser = await authApi.me(token);
        setUser(currentUser);
      } catch {
        tokenStorage.remove();
      } finally {
        setIsLoading(false);
      }
    };

    initAuth();
  }, []);

  const login = async (credentials: LoginCredentials) => {
    const response = await authApi.login(credentials);
    tokenStorage.set(response.token);
    setUser(response.user);
  };

  const logout = () => {
    tokenStorage.remove();
    setUser(null);
  };

  return (
    <AuthContext.Provider
      value={{
        user,
        isAuthenticated: !!user,
        isLoading,
        login,
        logout,
      }}
    >
      {children}
    </AuthContext.Provider>
  );
}
