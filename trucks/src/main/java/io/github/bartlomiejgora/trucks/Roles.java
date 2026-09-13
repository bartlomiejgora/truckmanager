package io.github.bartlomiejgora.trucks;

/**
 * Nazwy rol realmu {@code truckmanager} w Keycloaku (bez prefiksu {@code ROLE_},
 * ktory dokladaja metody {@code hasRole} / {@code hasAnyRole}).
 */
final class Roles {

    static final String ADMIN = "ADMIN";
    static final String KIEROWCA = "KIEROWCA";
    static final String WLASCICIEL_FIRMY = "WLASCICIEL_FIRMY";

    private Roles() {
    }
}
