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

Nous choisissons de développer une application web de type **Single Page Application (SPA)** à l’aide de **React**.

Le périmètre fonctionnel du frontend comprend :
- Authentification des utilisateurs et adaptation des vues selon les rôles (employé, secrétaire/admin, manager)
- Réservation de places de parking avec distinction entre places classiques et électriques
- Consultation des réservations à venir et de l’historique
- Vue administrative permettant la consultation et la modification des réservations
- Parcours de check-in via scan de QR code ou accès par URL dédiée pour confirmer l’occupation d’une place

## Justification

- **Maîtrise de l’équipe** : React est le framework frontend le mieux connu par l’équipe, ce qui réduit le risque et facilite le développement.
- **Adapté à une application interne** : le modèle SPA offre une navigation fluide et réactive, adaptée à des utilisateurs non techniques effectuant des actions fréquentes (réservation, check-in, consultation).
- **Architecture orientée composants** : React permet la réutilisation de composants (formulaires, tableaux, calendriers) entre les différentes vues et profils.
- **Intégration fonctionnelle** : React s’intègre facilement avec des API REST et des fonctionnalités spécifiques telles que le parcours de check-in via QR code.

## Alternatives considérées

- **Angular** : framework complet mais plus complexe à configurer et moins maîtrisé par l’équipe, ce qui augmente le risque et le temps de développement.
- **Rendu serveur (Spring MVC / Thymeleaf)** : approche plus simple côté frontend, mais moins adaptée aux interactions dynamiques, à la gestion des rôles et au parcours de check-in fluide requis par l’application.

## Conséquences

- Positives :
    - expérience utilisateur fluide sans rechargement de page
    - cohérence des interfaces entre les différents profils
- Négatives :
    - nécessité de gérer correctement l’authentification et la protection des routes côté frontend
    - besoin d’une gestion rigoureuse de l’état applicatif (utilisateur connecté, réservations, rôles)

