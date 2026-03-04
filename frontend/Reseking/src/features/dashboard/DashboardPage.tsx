import { useState, useEffect } from "react";
import { dashboardApi } from "./dashboard.api";
import type { DashboardStats, DateRange } from "./dashboard.types";
import "./dashboard.css";

const getDefaultDateRange = (): DateRange => {
  const today = new Date();
  const firstDayOfMonth = new Date(today.getFullYear(), today.getMonth(), 1);

  return {
    from: firstDayOfMonth.toISOString().split("T")[0],
    to: today.toISOString().split("T")[0],
  };
};

export function DashboardPage() {
  const [stats, setStats] = useState<DashboardStats | null>(null);
  const [dateRange, setDateRange] = useState<DateRange>(getDefaultDateRange());
  const [isLoading, setIsLoading] = useState(false);
  const [error, setError] = useState<string | null>(null);

  const fetchStats = async () => {
    setIsLoading(true);
    setError(null);
    try {
      const data = await dashboardApi.getStats(dateRange);
      setStats(data);
    } catch (err) {
      setError(err instanceof Error ? err.message : "Erreur inconnue");
    } finally {
      setIsLoading(false);
    }
  };

  useEffect(() => {
    fetchStats();
  }, []);

  const handleDateChange = (field: keyof DateRange, value: string) => {
    setDateRange((prev) => ({ ...prev, [field]: value }));
  };

  const handleSubmit = (e: React.FormEvent) => {
    e.preventDefault();
    fetchStats();
  };

  return (
    <div className="dashboard">
      <h2>Dashboard Manager</h2>

      <form className="dashboard-filters" onSubmit={handleSubmit}>
        <div className="date-inputs">
          <label>
            Du
            <input
              type="date"
              value={dateRange.from}
              onChange={(e) => handleDateChange("from", e.target.value)}
            />
          </label>
          <label>
            Au
            <input
              type="date"
              value={dateRange.to}
              onChange={(e) => handleDateChange("to", e.target.value)}
            />
          </label>
        </div>
        <button type="submit" disabled={isLoading}>
          {isLoading ? "Chargement..." : "Actualiser"}
        </button>
      </form>

      {error && <div className="dashboard-error">{error}</div>}

      {stats && (
        <div className="dashboard-grid">
          <div className="kpi-card">
            <span className="kpi-value">{stats.uniqueUsers}</span>
            <span className="kpi-label">Utilisateurs actifs</span>
          </div>

          <div className="kpi-card">
            <span className="kpi-value">{stats.averageOccupancyRate.toFixed(1)}%</span>
            <span className="kpi-label">Taux de remplissage</span>
          </div>

          <div className="kpi-card">
            <span className="kpi-value">{stats.noShowRate.toFixed(1)}%</span>
            <span className="kpi-label">Taux de no-show</span>
          </div>

          <div className="kpi-card">
            <span className="kpi-value">{stats.electricSpotRatio.toFixed(1)}%</span>
            <span className="kpi-label">Places electriques</span>
          </div>
        </div>
      )}
    </div>
  );
}
