import { useNavigate } from "react-router-dom";
import { LoginForm } from "./components/LoginForm";
import { useAuth } from "./useAuth";
import type { LoginCredentials } from "./auth.types";
import "./auth.css";

export function LoginPage() {
  const { login } = useAuth();
  const navigate = useNavigate();

  const handleLogin = async (credentials: LoginCredentials) => {
    await login(credentials);
    navigate("/");
  };

  return (
    <div className="login-page">
      <h1>Connexion</h1>
      <p>Connectez-vous pour accéder à votre espace de réservation</p>
      <LoginForm onSubmit={handleLogin} />
    </div>
  );
}
