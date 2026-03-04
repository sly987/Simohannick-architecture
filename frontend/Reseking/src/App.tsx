import { Routes, Route, NavLink } from "react-router-dom";
import { LoginPage } from "./features/auth/LoginPage";
import { ProfilePage } from "./features/auth/ProfilePage";
import { ProtectedRoute } from "./features/auth/components/ProtectedRoute";
import { useAuth } from "./features/auth/useAuth";
import ReservationPage from "./features/reservation/reservationPage";
import { HistoryPage } from "./features/reservation/HistoryPage";
import { DashboardPage } from "./features/dashboard/DashboardPage";
import { SecretaryPage } from "./features/secretary/SecretaryPage";
import { CheckInPage } from "./features/checkin/CheckInPage";
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
          {isAuthenticated && (
            <NavLink to="/history">Mes réservations</NavLink>
          )}
          {isAuthenticated && (
            <NavLink to="/check-in">Check-in</NavLink>
          )}
          {isAuthenticated && (user?.role === "MANAGER" || user?.role === "ADMIN") && (
            <NavLink to="/dashboard">Dashboard</NavLink>
          )}
          {isAuthenticated && user?.role === "ADMIN" && (
            <NavLink to="/secretary">Back-office</NavLink>
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
            <Route path="/history" element={<HistoryPage />} />
            <Route path="/profile" element={<ProfilePage />} />
            <Route path="/check-in" element={<CheckInPage />} />
            <Route path="/check-in/:row/:column" element={<CheckInPage />} />
          </Route>

          <Route element={<ProtectedRoute allowedRoles={["MANAGER", "ADMIN"]} />}>
            <Route path="/dashboard" element={<DashboardPage />} />
          </Route>

          <Route element={<ProtectedRoute allowedRoles={["ADMIN"]} />}>
            <Route path="/secretary" element={<SecretaryPage />} />
          </Route>
        </Routes>
      </main>
    </div>
  );
}

export default App;
