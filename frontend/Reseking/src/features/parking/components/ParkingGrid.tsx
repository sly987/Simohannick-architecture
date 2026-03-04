import type { ParkingSpot } from "../parking.types";
import "../ParkingGrid.css";

type Props = {
  spots: ParkingSpot[];
  onSelectSpot: (spot: ParkingSpot) => void;
};

export default function ParkingGrid({ spots, onSelectSpot }: Props) {
  const sortedSpots = [...spots].sort((a, b) => {
    const rowA = a.row ?? "?";
    const rowB = b.row ?? "?";
    const colA = a.column ?? "0";
    const colB = b.column ?? "0";
    const rowCompare = rowA.localeCompare(rowB);
    return rowCompare !== 0 ? rowCompare : colA - colB;
  });

  const rowsMap: Record<string, ParkingSpot[]> = {};
  sortedSpots.forEach((spot) => {
    const row = spot.row ?? "?";
    if (!rowsMap[row]) rowsMap[row] = [];
    rowsMap[row].push(spot);
  });

  const rowKeys = Object.keys(rowsMap).sort();

  return (
    <div className="parking-grid-container">
      <div className="parking-grid">
        {rowKeys.map((row) => (
          <div key={row} className="parking-row">
            {rowsMap[row].map((spot) => {
              const isOccupied = spot.activeReservation != null;
              return (
                <div
                  key={spot.id}
                  className={`parking-spot ${isOccupied ? "occupied" : "free"} ${spot.type.toLowerCase()}`}
                  onClick={() => !isOccupied && onSelectSpot(spot)}
                >
                  <div className="spot-info">
                    {spot.row}{spot.column}
                    {spot.type === "ELECTRIC" && (
                      <span className="electric-icon">⚡</span>
                    )}
                  </div>
                </div>
              );
            })}
          </div>
        ))}
      </div>
    </div>
  );
}