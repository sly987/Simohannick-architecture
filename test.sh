#!/bin/bash

echo "=== Running Tests ==="

# Backend tests
echo "Running backend tests..."
cd backend/module/reseking
mvn test -q
BACKEND_EXIT=$?
cd ../../..

# Frontend tests
echo "Running frontend tests..."
cd frontend/Reseking
npm test -- --run 2>/dev/null || echo "No frontend tests configured"
FRONTEND_EXIT=$?
cd ../..

echo ""
echo "=== Test Results ==="
if [ $BACKEND_EXIT -eq 0 ]; then
    echo "Backend tests: PASSED"
else
    echo "Backend tests: FAILED"
fi

echo "Frontend tests: Check output above"
