-- 0) Drop tables (order matters due to FKs)
DROP TABLE IF EXISTS reservation_days CASCADE;
DROP TABLE IF EXISTS reservations CASCADE;
DROP TABLE IF EXISTS parking_spot CASCADE;
DROP TABLE IF EXISTS employee CASCADE;

-- 1) Employee table
CREATE TABLE employee (
                          employee_id SERIAL PRIMARY KEY,
                          first_name VARCHAR(100) NOT NULL,
                          last_name  VARCHAR(100) NOT NULL,
                          email      VARCHAR(150) NOT NULL UNIQUE,
                          password   VARCHAR(255) NOT NULL,
                          role       VARCHAR(20)  NOT NULL CHECK (role IN ('EMPLOYEE', 'MANAGER', 'ADMIN'))
);

-- 2) Parking spot table
CREATE TABLE parking_spot (
                              spot_id SERIAL PRIMARY KEY,
                              spot_row CHAR(1) NOT NULL CHECK (spot_row IN ('A','B','C','D','E','F')),
                              spot_column CHAR(2) NOT NULL CHECK (spot_column IN ('01','02','03','04','05','06','07','08','09','10')),
                              type VARCHAR(20) NOT NULL CHECK (type IN ('STANDARD', 'ELECTRIC')),
                              CONSTRAINT uk_spot_location UNIQUE (spot_row, spot_column)
);

-- 3) Reservations table
CREATE TABLE reservations (
                              reservation_id SERIAL PRIMARY KEY,
                              start_date DATE NOT NULL,
                              end_date   DATE NOT NULL,
                              registration_number VARCHAR(50) NOT NULL,

                              status VARCHAR(20) NOT NULL DEFAULT 'BOOKED'
                                  CHECK (status IN ('BOOKED', 'CANCELLED', 'COMPLETED')),

                              employee_id INT NOT NULL,
                              spot_id     INT NOT NULL,

                              CONSTRAINT fk_reservation_employee
                                  FOREIGN KEY (employee_id)
                                      REFERENCES employee(employee_id)
                                      ON DELETE CASCADE,

                              CONSTRAINT fk_reservation_spot
                                  FOREIGN KEY (spot_id)
                                      REFERENCES parking_spot(spot_id)
                                      ON DELETE CASCADE,

                              CONSTRAINT chk_date_range CHECK (start_date <= end_date)
);

-- 4) Reservation days table
CREATE TABLE reservation_days (
                                  id SERIAL PRIMARY KEY,

                                  reservation_id INT NOT NULL,
                                  spot_id INT NOT NULL,

                                  date DATE NOT NULL,

                                  status VARCHAR(20) NOT NULL
                                      CHECK (status IN ('BOOKED', 'CHECKED_IN', 'FORFEITED')),

                                  checked_in_at TIMESTAMP NULL,

                                  CONSTRAINT fk_res_day_reservation
                                      FOREIGN KEY (reservation_id)
                                          REFERENCES reservations(reservation_id)
                                          ON DELETE CASCADE,

                                  CONSTRAINT fk_res_day_spot
                                      FOREIGN KEY (spot_id)
                                          REFERENCES parking_spot(spot_id)
                                          ON DELETE CASCADE
);

CREATE UNIQUE INDEX ux_reservation_days_spot_date_active
    ON reservation_days (spot_id, date)
    WHERE status IN ('BOOKED', 'CHECKED_IN');

-- ============================================================
-- Helpful indexes
-- ============================================================
CREATE INDEX idx_employee_email ON employee(email);

CREATE INDEX idx_reservation_employee ON reservations(employee_id);
CREATE INDEX idx_reservation_spot ON reservations(spot_id);
CREATE INDEX idx_reservation_status ON reservations(status);
CREATE INDEX idx_reservation_dates ON reservations(start_date, end_date);

CREATE INDEX idx_res_day_reservation ON reservation_days(reservation_id);
CREATE INDEX idx_res_day_spot_date ON reservation_days(spot_id, date);
CREATE INDEX idx_res_day_date_status ON reservation_days(date, status);