import { Navigate, Outlet } from "react-router-dom";
import { useAuth } from "../useAuth";
import type { Role } from "../auth.types";

type ProtectedRouteProps = {
  allowedRoles?: Role[];
};

export function ProtectedRoute({ allowedRoles }: ProtectedRouteProps) {
  const { user, isAuthenticated, isLoading } = useAuth();

  if (isLoading) {
    return <div>Chargement...</div>;
  }

  if (!isAuthenticated) {
    return <Navigate to="/login" replace />;
  }

  if (allowedRoles && user && !allowedRoles.includes(user.role)) {
    return <Navigate to="/" replace />;
  }

  return <Outlet />;
}
