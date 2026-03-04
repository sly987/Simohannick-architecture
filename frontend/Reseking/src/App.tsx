import { Routes, Route, NavLink } from "react-router-dom";
import { LoginPage } from "./features/auth/LoginPage";
import { ProfilePage } from "./features/auth/ProfilePage";
import { ProtectedRoute } from "./features/auth/components/ProtectedRoute";
import { useAuth } from "./features/auth/useAuth";
import ReservationPage from "./features/reservation/reservationPage";
import { DashboardPage } from "./features/dashboard/DashboardPage";
import "./App.css";

function App() {
  const { user, isAuthenticated, logout } = useAuth();

  return (
    <div className="app">
      <header className="header">
        <h1>Reseking</h1>

        <nav>
          <NavLink to="/" end>
            Accueil
          </NavLink>
          {isAuthenticated && (
            <NavLink to="/reservation">Réserver</NavLink>
          )}
          {isAuthenticated && (user?.role === "MANAGER" || user?.role === "ADMIN") && (
            <NavLink to="/dashboard">Dashboard</NavLink>
          )}
        </nav>

        <div className="auth-section">
          {isAuthenticated ? (
            <>
              <NavLink to="/profile">{user?.firstName} {user?.lastName}</NavLink>
              <button onClick={logout}>Déconnexion</button>
            </>
          ) : (
            <NavLink to="/login">Connexion</NavLink>
          )}
        </div>
      </header>

      <main className="main">
        <Routes>
          <Route
            path="/"
            element={
              <div className="home">
                <h2>Bienvenue sur Reseking</h2>
                <p>Réservez votre place de parking en quelques secondes.</p>
              </div>
            }
          />

          <Route path="/login" element={<LoginPage />} />

          <Route element={<ProtectedRoute />}>
            <Route path="/reservation" element={<ReservationPage />} />
            <Route path="/profile" element={<ProfilePage />} />
          </Route>

          <Route element={<ProtectedRoute allowedRoles={["MANAGER", "ADMIN"]} />}>
            <Route path="/dashboard" element={<DashboardPage />} />
          </Route>
        </Routes>
      </main>
    </div>
  );
}

export default App;
