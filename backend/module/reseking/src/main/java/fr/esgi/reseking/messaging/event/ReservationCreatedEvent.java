package fr.esgi.reseking.messaging.event;

import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReservationCreatedEvent {
    private Integer reservationId;
    private String employeeEmail;
    private String spotLabel;
    private LocalDate startDate;
    private LocalDate endDate;

    private ReservationCreatedEvent() {}

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ReservationCreatedEvent event;

        private Builder() {
            this.event = new ReservationCreatedEvent();
        }

        public Builder reservationId(Integer reservationId) {
            event.reservationId = reservationId;
            return this;
        }

        public Builder employeeEmail(String employeeEmail) {
            event.employeeEmail = employeeEmail;
            return this;
        }

        public Builder spotLabel(String spotLabel) {
            event.spotLabel = spotLabel;
            return this;
        }

        public Builder startDate(LocalDate startDate) {
            event.startDate = startDate;
            return this;
        }

        public Builder endDate(LocalDate endDate) {
            event.endDate = endDate;
            return this;
        }

        public ReservationCreatedEvent build() {
            return event;
        }
    }
}
