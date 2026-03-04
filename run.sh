#!/bin/bash

echo "=== Starting Reseking Application ==="

# Start all services
docker-compose up -d

echo ""
echo "Services started:"
echo "  - Frontend: http://localhost:3000"
echo "  - Backend:  http://localhost:8080"
echo "  - Database: localhost:5432"
echo "  - RabbitMQ: http://localhost:15672"
echo ""
echo "Use 'docker-compose logs -f' to view logs"
echo "Use 'docker-compose down' to stop"
