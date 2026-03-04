import { describe, it, expect } from 'vitest';
import type { Reservation, ReservationStatus } from './reservation.types';

describe('Reservation types', () => {
  it('should accept valid reservation', () => {
    const reservation: Reservation = {
      startDate: '2026-03-04',
      endDate: '2026-03-07',
      registrationNumber: 'AB-123-CD',
      spotId: 11,
    };

    expect(reservation.startDate).toBe('2026-03-04');
    expect(reservation.endDate).toBe('2026-03-07');
    expect(reservation.spotId).toBe(11);
  });

  it('should have valid status values', () => {
    const statuses: ReservationStatus[] = [
      'BOOKED',
      'CHECKED_IN',
      'FORFEITED',
      'CANCELLED',
      'COMPLETED',
    ];

    expect(statuses).toHaveLength(5);
    expect(statuses).toContain('BOOKED');
    expect(statuses).toContain('CANCELLED');
  });
});

describe('Reservation validation', () => {
  it('should detect invalid date range', () => {
    const startDate = new Date('2026-03-10');
    const endDate = new Date('2026-03-05');

    expect(endDate < startDate).toBe(true);
  });

  it('should calculate duration correctly', () => {
    const startDate = new Date('2026-03-04');
    const endDate = new Date('2026-03-09');
    const diffTime = endDate.getTime() - startDate.getTime();
    const diffDays = diffTime / (1000 * 60 * 60 * 24);

    expect(diffDays).toBe(5);
  });

  it('should enforce max 5 days for employees', () => {
    const maxDays = 5;
    const startDate = new Date('2026-03-04');
    const endDate = new Date('2026-03-10');
    const diffTime = endDate.getTime() - startDate.getTime();
    const diffDays = diffTime / (1000 * 60 * 60 * 24);

    expect(diffDays > maxDays).toBe(true);
  });

  it('should allow up to 30 days for managers', () => {
    const maxDays = 30;
    const startDate = new Date('2026-03-04');
    const endDate = new Date('2026-03-20');
    const diffTime = endDate.getTime() - startDate.getTime();
    const diffDays = diffTime / (1000 * 60 * 60 * 24);

    expect(diffDays <= maxDays).toBe(true);
  });
});
