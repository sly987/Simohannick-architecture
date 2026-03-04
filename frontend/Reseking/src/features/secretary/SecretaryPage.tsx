import { useEffect, useState } from "react";
import type { Reservation, ReservationStatus } from "../reservation/reservation.types";
import { secretaryApi, type Employee, type ParkingSpot } from "./secretary.api";
import "./secretary.css";

const STATUS_LABELS: Record<ReservationStatus, string> = {
  BOOKED: "Réservé",
  CHECKED_IN: "Enregistré",
  FORFEITED: "Forfait",
  CANCELLED: "Annulé",
  COMPLETED: "Terminé",
};

const STATUS_COLORS: Record<ReservationStatus, string> = {
  BOOKED: "#3498db",
  CHECKED_IN: "#27ae60",
  FORFEITED: "#e74c3c",
  CANCELLED: "#f39c12",
  COMPLETED: "#95a5a6",
};

export function SecretaryPage() {
  const [activeTab, setActiveTab] = useState<"reservations" | "employees">("reservations");
  const [reservations, setReservations] = useState<Reservation[]>([]);
  const [employees, setEmployees] = useState<Map<number, Employee>>(new Map());
  const [employeesList, setEmployeesList] = useState<Employee[]>([]);
  const [spots, setSpots] = useState<Map<number, ParkingSpot>>(new Map());
  const [filterType, setFilterType] = useState<"employee" | "spot">("employee");
  const [filterValue, setFilterValue] = useState("");
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState<string | null>(null);

  // Form state for adding employee
  const [newEmployee, setNewEmployee] = useState({
    firstName: "",
    lastName: "",
    email: "",
    password: "",
    role: "EMPLOYEE",
  });
  const [formError, setFormError] = useState<string | null>(null);

  const loadData = async () => {
    try {
      setLoading(true);
      const [reservationsData, employeesData, spotsData] = await Promise.all([
        secretaryApi.getAllReservations(),
        secretaryApi.getAllEmployees(),
        secretaryApi.getAllSpots(),
      ]);
      setReservations(reservationsData);
      setEmployeesList(employeesData);
      const employeeMap = new Map<number, Employee>();
      employeesData.forEach((e) => employeeMap.set(e.id, e));
      setEmployees(employeeMap);
      const spotMap = new Map<number, ParkingSpot>();
      spotsData.forEach((s) => spotMap.set(s.id, s));
      setSpots(spotMap);
      setError(null);
    } catch {
      setError("Erreur lors du chargement des données");
    } finally {
      setLoading(false);
    }
  };

  useEffect(() => {
    loadData();
  }, []);

  const handleCancel = async (id: number) => {
    if (!confirm("Voulez-vous vraiment annuler cette réservation ?")) return;
    try {
      await secretaryApi.cancelReservation(id);
      await loadData();
    } catch {
      alert("Erreur lors de l'annulation");
    }
  };

  const handleDelete = async (id: number) => {
    if (!confirm("Voulez-vous vraiment supprimer cette réservation ?")) return;
    try {
      await secretaryApi.deleteReservation(id);
      await loadData();
    } catch {
      alert("Erreur lors de la suppression");
    }
  };

  const handleAddEmployee = async (e: React.FormEvent) => {
    e.preventDefault();
    setFormError(null);

    if (!newEmployee.firstName || !newEmployee.lastName || !newEmployee.email || !newEmployee.password) {
      setFormError("Tous les champs sont obligatoires");
      return;
    }

    try {
      await secretaryApi.addEmployee(newEmployee);
      setNewEmployee({ firstName: "", lastName: "", email: "", password: "", role: "EMPLOYEE" });
      await loadData();
    } catch {
      setFormError("Erreur lors de la création de l'employé");
    }
  };

  const getEmployeeName = (employeeId: number | undefined) => {
    if (!employeeId) return "-";
    const employee = employees.get(employeeId);
    return employee ? `${employee.firstName} ${employee.lastName}` : `#${employeeId}`;
  };

  const getSpotLabel = (spotId: number) => {
    const spot = spots.get(spotId);
    return spot ? `${spot.row}${spot.column}` : `#${spotId}`;
  };

  const filteredReservations = reservations.filter((r) => {
    if (!filterValue) return true;
    if (filterType === "employee") {
      return r.employeeId === Number(filterValue);
    } else {
      return r.spotId === Number(filterValue);
    }
  });

  if (loading) return <div className="secretary-loading">Chargement...</div>;
  if (error) return <div className="secretary-error">{error}</div>;

  return (
    <div className="secretary">
      <h2>Back-office</h2>

      <div className="secretary-tabs">
        <button
          className={activeTab === "reservations" ? "active" : ""}
          onClick={() => setActiveTab("reservations")}
        >
          Réservations
        </button>
        <button
          className={activeTab === "employees" ? "active" : ""}
          onClick={() => setActiveTab("employees")}
        >
          Employés
        </button>
      </div>

      {activeTab === "reservations" && (
        <>
          <div className="secretary-filter">
            <select
              value={filterType}
              onChange={(e) => {
                setFilterType(e.target.value as "employee" | "spot");
                setFilterValue("");
              }}
            >
              <option value="employee">Par employé</option>
              <option value="spot">Par place</option>
            </select>

            <select
              value={filterValue}
              onChange={(e) => setFilterValue(e.target.value)}
            >
              <option value="">Tous</option>
              {filterType === "employee"
                ? Array.from(employees.values()).map((e) => (
                    <option key={e.id} value={e.id}>
                      {e.firstName} {e.lastName}
                    </option>
                  ))
                : Array.from(spots.values()).map((s) => (
                    <option key={s.id} value={s.id}>
                      {s.row}{s.column}
                    </option>
                  ))}
            </select>
          </div>

          {filteredReservations.length === 0 ? (
            <p className="secretary-empty">Aucune réservation.</p>
          ) : (
            <table className="secretary-table">
              <thead>
                <tr>
                  <th>Place</th>
                  <th>Employé</th>
                  <th>Immatriculation</th>
                  <th>Début</th>
                  <th>Fin</th>
                  <th>Statut</th>
                  <th>Actions</th>
                </tr>
              </thead>
              <tbody>
                {filteredReservations.map((r) => (
                  <tr key={r.id}>
                    <td>{getSpotLabel(r.spotId)}</td>
                    <td>{getEmployeeName(r.employeeId)}</td>
                    <td>{r.registrationNumber}</td>
                    <td>{r.startDate}</td>
                    <td>{r.endDate}</td>
                    <td>
                      <span
                        className="secretary-status"
                        style={{ backgroundColor: STATUS_COLORS[r.status!] }}
                      >
                        {STATUS_LABELS[r.status!]}
                      </span>
                    </td>
                    <td className="secretary-actions">
                      {r.status === "BOOKED" && (
                        <button
                          className="secretary-cancel-btn"
                          onClick={() => handleCancel(r.id!)}
                        >
                          Annuler
                        </button>
                      )}
                      <button
                        className="secretary-delete-btn"
                        onClick={() => handleDelete(r.id!)}
                      >
                        Supprimer
                      </button>
                    </td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </>
      )}

      {activeTab === "employees" && (
        <>
          <div className="secretary-add-form">
            <h3>Ajouter un employé</h3>
            <form onSubmit={handleAddEmployee}>
              <div className="form-row">
                <input
                  type="text"
                  placeholder="Prénom"
                  value={newEmployee.firstName}
                  onChange={(e) => setNewEmployee({ ...newEmployee, firstName: e.target.value })}
                />
                <input
                  type="text"
                  placeholder="Nom"
                  value={newEmployee.lastName}
                  onChange={(e) => setNewEmployee({ ...newEmployee, lastName: e.target.value })}
                />
              </div>
              <div className="form-row">
                <input
                  type="email"
                  placeholder="Email"
                  value={newEmployee.email}
                  onChange={(e) => setNewEmployee({ ...newEmployee, email: e.target.value })}
                />
                <input
                  type="password"
                  placeholder="Mot de passe"
                  value={newEmployee.password}
                  onChange={(e) => setNewEmployee({ ...newEmployee, password: e.target.value })}
                />
              </div>
              <div className="form-row">
                <select
                  value={newEmployee.role}
                  onChange={(e) => setNewEmployee({ ...newEmployee, role: e.target.value })}
                >
                  <option value="EMPLOYEE">Employé</option>
                  <option value="MANAGER">Manager</option>
                  <option value="ADMIN">Admin</option>
                </select>
                <button type="submit">Ajouter</button>
              </div>
              {formError && <p className="form-error">{formError}</p>}
            </form>
          </div>

          <h3>Liste des employés</h3>
          {employeesList.length === 0 ? (
            <p className="secretary-empty">Aucun employé.</p>
          ) : (
            <table className="secretary-table">
              <thead>
                <tr>
                  <th>Nom</th>
                  <th>Email</th>
                  <th>Rôle</th>
                </tr>
              </thead>
              <tbody>
                {employeesList.map((e) => (
                  <tr key={e.id}>
                    <td>{e.firstName} {e.lastName}</td>
                    <td>{e.email}</td>
                    <td>{e.role}</td>
                  </tr>
                ))}
              </tbody>
            </table>
          )}
        </>
      )}
    </div>
  );
}
