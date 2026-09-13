-- Keycloak trzyma swoje dane w osobnej bazie na tej samej instancji Postgresa.
-- Skrypt uruchamia sie tylko przy inicjalizacji pustego wolumenu postgres-data.
CREATE DATABASE keycloakdb;
