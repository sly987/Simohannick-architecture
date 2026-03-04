import { useAuth } from "./useAuth";
import "./auth.css";

const ROLE_LABELS: Record<string, string> = {
  ADMIN: "Administrateur",
  MANAGER: "Manager",
  EMPLOYEE: "Employé",
};

export function ProfilePage() {
  const { user, logout } = useAuth();

  if (!user) return null;

  return (
    <div className="profile-page">
      <h1>Mon profil</h1>

      <div className="profile-card">
        <div className="profile-field">
          <span className="label">Nom</span>
          <span className="value">{user.lastName}</span>
        </div>

        <div className="profile-field">
          <span className="label">Prénom</span>
          <span className="value">{user.firstName}</span>
        </div>

        <div className="profile-field">
          <span className="label">Email</span>
          <span className="value">{user.email}</span>
        </div>

        <div className="profile-field">
          <span className="label">Rôle</span>
          <span className="value">{ROLE_LABELS[user.role]}</span>
        </div>

        <button onClick={logout} className="logout-btn">
          Se déconnecter
        </button>
      </div>
    </div>
  );
}
