import { Routes, Route, NavLink } from "react-router-dom";
import ReservationPage from "./pages/ReservationPage";
import "./App.css";

function App() {
    return (
        <div className="app">
            <header className="header">
                <h1>🚘 Reseking</h1>

                <nav>
                    <NavLink to="/" end>
                        Accueil
                    </NavLink>
                    <NavLink to="/reservation">
                        Réserver
                    </NavLink>
                </nav>
            </header>

            <main className="main">
                <Routes>
                    <Route
                        path="/"
                        element={
                            <div className="home">
                                <h2>Bienvenue sur Reseking</h2>
                                <p>
                                    Réservez votre place de parking en quelques secondes.
                                </p>
                            </div>
                        }
                    />

                    <Route path="/reservation" element={<ReservationPage />} />
                </Routes>
            </main>
        </div>
    );
}

export default App;
