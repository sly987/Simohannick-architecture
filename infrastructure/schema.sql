-- 1. Employee table
CREATE TABLE employee (
                          employee_id SERIAL PRIMARY KEY,
                          first_name VARCHAR(100) NOT NULL,
                          last_name VARCHAR(100) NOT NULL,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          role VARCHAR(20) NOT NULL CHECK (role IN ('EMPLOYEE', 'MANAGER', 'ADMIN'))
);

-- 2. Parking spot table
CREATE TABLE parking_spot (
                              spot_id SERIAL PRIMARY KEY,
                              spot_row CHAR(1) NOT NULL,
                              spot_column CHAR(2) NOT NULL,
                              type VARCHAR(20) NOT NULL CHECK (type IN ('STANDARD', 'ELECTRIC')),
                              CONSTRAINT uk_spot_location UNIQUE (spot_row, spot_column)
);

-- 3. Reservations table
CREATE TABLE reservations (
                              reservation_id SERIAL PRIMARY KEY,
                              start_date DATE NOT NULL,
                              end_date DATE NOT NULL,
                              registration_number VARCHAR(50) NOT NULL,
                              status VARCHAR(20) NOT NULL DEFAULT 'BOOKED' CHECK (status IN ('BOOKED', 'CHECKED_IN', 'FORFEITED', 'CANCELLED', 'COMPLETED')),
                              employee_id INT NOT NULL,
                              spot_id INT NOT NULL,
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

-- 4. Check-ins table (tracks check-ins for each day of a reservation)
CREATE TABLE check_ins (
                           reservation_id INT NOT NULL,
                           check_in_date DATE NOT NULL,
                           checked_in_at TIMESTAMP NOT NULL,
                           PRIMARY KEY (reservation_id, check_in_date),
                           CONSTRAINT fk_check_in_reservation
                               FOREIGN KEY (reservation_id)
                                   REFERENCES reservations(reservation_id)
                                   ON DELETE CASCADE
);

-- Indexes for better query performance
CREATE INDEX idx_employee_email ON employee(email);
CREATE INDEX idx_reservation_employee ON reservations(employee_id);
CREATE INDEX idx_reservation_spot ON reservations(spot_id);
CREATE INDEX idx_reservation_status ON reservations(status);
CREATE INDEX idx_reservation_dates ON reservations(start_date, end_date);
CREATE INDEX idx_check_in_date ON check_ins(check_in_date);
