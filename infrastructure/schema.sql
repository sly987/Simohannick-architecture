-- 1. Employee table without active_reservation FK
CREATE TABLE employee (
                          employee_id SERIAL PRIMARY KEY,
                          first_name VARCHAR(100) NOT NULL,
                          last_name VARCHAR(100) NOT NULL,
                          email VARCHAR(150) NOT NULL UNIQUE,
                          password VARCHAR(255) NOT NULL,
                          role VARCHAR(20) NOT NULL,
                          active_reservation_id INT DEFAULT NULL
);

-- 2. Parking spot table without active_reservation FK
CREATE TABLE parking_spot (
                              spot_id SERIAL PRIMARY KEY,
                              spot_row CHAR(1) NOT NULL,
                              spot_column CHAR(2) NOT NULL,
                              type VARCHAR(20) NOT NULL,
                              active_reservation_id INT DEFAULT NULL
);

-- 3. Reservations table
CREATE TABLE reservations (
                              reservation_id SERIAL PRIMARY KEY,
                              start_date VARCHAR(50) NOT NULL,
                              duration INT NOT NULL,
                              registration_number VARCHAR(50) NOT NULL,
                              employee_id INT DEFAULT NULL,
                              spot_id INT DEFAULT NULL,
                              CONSTRAINT fk_reservation_employee
                                  FOREIGN KEY (employee_id)
                                      REFERENCES employee(employee_id)
                                      ON DELETE SET NULL,
                              CONSTRAINT fk_reservation_spot
                                  FOREIGN KEY (spot_id)
                                      REFERENCES parking_spot(spot_id)
                                      ON DELETE SET NULL
);

-- 4. Now add the active_reservation FKs
ALTER TABLE employee
    ADD CONSTRAINT fk_employee_active_reservation
        FOREIGN KEY (active_reservation_id)
            REFERENCES reservations(reservation_id)
            ON DELETE SET NULL;

ALTER TABLE parking_spot
    ADD CONSTRAINT fk_parking_active_reservation
        FOREIGN KEY (active_reservation_id)
            REFERENCES reservations(reservation_id)
            ON DELETE SET NULL;