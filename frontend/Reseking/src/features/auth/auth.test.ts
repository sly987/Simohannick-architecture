import { describe, it, expect, beforeEach } from 'vitest';
import { tokenStorage } from './auth.api';

describe('tokenStorage', () => {
  beforeEach(() => {
    localStorage.clear();
  });

  it('should store and retrieve token', () => {
    tokenStorage.set('test-token');
    expect(tokenStorage.get()).toBe('test-token');
  });

  it('should return null when no token', () => {
    expect(tokenStorage.get()).toBeNull();
  });

  it('should remove token', () => {
    tokenStorage.set('test-token');
    tokenStorage.remove();
    expect(tokenStorage.get()).toBeNull();
  });
});
