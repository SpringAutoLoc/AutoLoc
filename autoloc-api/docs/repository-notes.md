# Repository notes — Atelier 3

## Interfaces créées

| Interface | Étend | Justification |
|---|---|---|
| IContratRepository | JpaRepository<Contrat, Long> | CRUD complet, findAll en List, saveAndFlush disponible |
| IPaiementRepository | JpaRepository<Paiement, Long> | Lecture seule en pratique ; création/suppression passent par Contrat |
| IAgenceRepository | JpaRepository<Agence, Long> | CRUD standard |
| IEmployeRepository | JpaRepository<Employe, Long> | CRUD standard |
| IVehiculeRepository | JpaRepository<Vehicule, Long> | CRUD standard |
| IEquipementRepository | JpaRepository<Equipement, Long> | CRUD standard |
| IClientRepository | JpaRepository<Client, Long> | CRUD standard |
| IReservationRepository | JpaRepository<Reservation, Long> | CRUD standard |
| IMaintenanceRepository | JpaRepository<Maintenance, Long> | CRUD standard |

## Anomalies SonarQube for IDE

rien