#!/bin/bash

echo "=== Building Reseking Application ==="

# Build backend
echo "Building backend..."
cd backend/module/reseking
mvn clean package -DskipTests -q
cd ../../..

# Build frontend
echo "Building frontend..."
cd frontend/Reseking
npm ci
npm run build
cd ../..

# Build Docker images
echo "Building Docker images..."
docker-compose build

echo "=== Build complete ==="
