## Contexte

L’entreprise souhaite remplacer un processus manuel de réservation de places de parking, actuellement géré par échanges d’emails et un fichier Excel, devenu inefficace depuis la mise en place du travail hybride.

Le projet consiste à développer une application interne destinée à des utilisateurs non techniques, permettant une gestion autonome et fiable des réservations, tout en réduisant la charge administrative et en offrant une meilleure visibilité à la direction.

### Fonctionnalités clés

- Authentification des utilisateurs et gestion des rôles (employé, secrétaire/admin, manager)
- Réservation de places de parking par créneaux
- Gestion spécifique des places avec bornes électriques
- Check-in obligatoire via QR code pour confirmer l’occupation d’une place
- Libération automatique d’une place en l’absence de check-in avant 11h
- Consultation des réservations actuelles et futures
- Conservation de l’historique complet des réservations
- Envoi d’un message dans une file pour déclencher l’envoi d’un email de confirmation

### Contraintes métier

- Parking composé de 60 places numérotées, réparties en 6 rangées (A à F)
- Les rangées A et F sont réservées aux véhicules électriques ou hybrides
- Les employés peuvent réserver une place pour un maximum de 5 jours ouvrés
- Les managers peuvent réserver une place pour une durée maximale de 30 jours
- Les employés doivent être autonomes dans la gestion de leurs réservations
- Les secrétaires disposent d’un accès administrateur complet

## Décision

Nous choisissons de développer le backend sous forme de deux services en Java avec Spring Boot :

- **Parking Service** : gestion des places, disponibilités, réservations, check-in, règles métier et historique
- **Notification Service** : traitement des demandes de notification et envoi des emails de confirmation

Les deux services communiquent de manière **asynchrone via une file de messages** : lors d’une réservation (ou d’un événement métier pertinent), le Parking Service publie un message, consommé ensuite par le Notification Service pour déclencher l’envoi de l’email.

La base de données choisie est **PostgreSQL**.

## Justification

- **Maîtrise de l’équipe** : Java est le langage le mieux connu par l’équipe, réduisant le risque projet.
- **Productivité et robustesse** : Spring Boot facilite la création d’API web (REST), la structuration du code, l’intégration avec une base de données et les bonnes pratiques d’architecture.
- **Séparation des responsabilités** : isoler la notification permet d’éviter que l’envoi d’emails ralentisse ou impacte le cœur métier des réservations.
- **Asynchronisme** : la file de messages permet de découpler le traitement (réservation vs notification) et d’absorber les pics sans bloquer l’utilisateur.
- **Habitude et compatibilité** : PostgreSQL est déjà maîtrisé par le groupe et adapté aux besoins transactionnels (réservations, intégrité, historique). De plus, le schema de la base n'intègre pas de structure de donnees dynamiques.

## Alternatives considérées

- **Monolithe unique** (Parking + Notifications) :
  - plus simple à déployer, mais moins isolé (risque que les notifications impactent le cœur métier)
- **Communication synchrone (HTTP) entre services** :
  - plus simple à comprendre, mais plus fragile (dépendance directe, risque de timeouts et d’indisponibilité)
- **Architecture micro service + Communication asynchrone** :
  - Decoupage plus fin du coeur du metier (Service Parking - Service de reservation - service de gestion des employees...etc). Pas de dépendances, mais contrainte de temps et une seule équipe de développement 

## Conséquences

- Positives :
  - séparation fonctionnelle des responsabilités 
  - notifications non bloquantes grâce à la file de messages
  - cohérence et fiabilité via PostgreSQL pour le domaine réservation
- Négatives :
  - complexité supplémentaire liée a la communication asynchrone (retries, dead-letter queue, idempotence)
  - manque de fléxibilté lie aux futurs changements de besoin (gestion de différents types de véhicules comme des vélos, motos…)
