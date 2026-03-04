# Reseking - Quick Start

## Prérequis

- Docker & Docker Compose
- Node.js 20+ (pour développement local)
- Java 21+ & Maven (pour développement local)

## Installation rapide (Docker)

1. **Cloner le projet**
```bash
git clone https://github.com/sly987/Simohannick-architecture.git
cd Simohannick-architecture
```

2. **Lancer l'application**
```bash
./run.sh
```

3. **Accéder à l'application**
- Frontend : http://localhost:3000
- Backend API : http://localhost:8080
- RabbitMQ : http://localhost:15672

## Comptes de test

| Email | Mot de passe | Rôle |
|-------|--------------|------|
| jane.smith@example.com | password123 | EMPLOYEE |
| john.doe@example.com | password123 | MANAGER |
| admin@company.com | password123 | ADMIN |

## Fonctionnalités principales

### Employé
- **Réserver une place** : Sélectionner une place sur la grille, choisir les dates (max 5 jours), entrer l'immatriculation
- **Voir son historique** : Consulter ses réservations passées et en cours
- **Check-in** : Confirmer son arrivée sur une place réservée

### Manager
- Toutes les fonctionnalités employé
- **Dashboard** : Voir les KPIs (utilisateurs, taux d'occupation, no-shows, places électriques)
- Réservation jusqu'à 30 jours

### Admin
- Toutes les fonctionnalités manager
- **Back-office** : Gérer les réservations (annuler, supprimer)
- **Gestion employés** : Créer de nouveaux employés

## Structure du parking

- **60 places** réparties en 6 rangées (A à F)
- **Rangées A et F** : Places avec bornes électriques (affichées en bleu)
- **Rangées B, C, D, E** : Places standard (affichées en vert)

## Scripts disponibles

| Script | Description |
|--------|-------------|
| `./build.sh` | Compile le backend, frontend et construit les images Docker |
| `./run.sh` | Lance tous les services (Docker Compose) |
| `./stop.sh` | Arrête tous les services |
| `./test.sh` | Lance les tests backend et frontend |

## Développement local

### Backend
```bash
cd backend/module/reseking
mvn spring-boot:run
```

### Frontend
```bash
cd frontend/Reseking
npm install
npm run dev
```

## Arrêter l'application

```bash
./stop.sh
# ou
docker-compose down
```
