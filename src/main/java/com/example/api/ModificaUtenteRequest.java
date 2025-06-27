package com.example.api;

public class ModificaUtenteRequest {

    // Vecchi dati identificativi
    private String vecchioUsername;
    private String vecchiaPasswordHash;

    // Nuovi dati
    private String nuovoUsername;
    private String nuovoNome;
    private String nuovoCognome;
    private String nuovaEmail;
    private String nuovaPasswordHash;
    private String nuovaBio;

    // Getter e Setter
    public String getVecchioUsername() {
        return vecchioUsername;
    }

    public void setVecchioUsername(String vecchioUsername) {
        this.vecchioUsername = vecchioUsername;
    }

    public String getNuovoUsername() {
        return nuovoUsername;
    }

    public void setNuovoUsername(String nuovoUsername) {
        this.nuovoUsername = nuovoUsername;
    }

    public String getNuovoNome() {
        return nuovoNome;
    }

    public void setNuovoNome(String nuovoNome) {
        this.nuovoNome = nuovoNome;
    }

    public String getNuovoCognome() {
        return nuovoCognome;
    }

    public void setNuovoCognome(String nuovoCognome) {
        this.nuovoCognome = nuovoCognome;
    }

    public String getNuovaEmail() {
        return nuovaEmail;
    }

    public void setNuovaEmail(String nuovaEmail) {
        this.nuovaEmail = nuovaEmail;
    }

    public String getNuovaPasswordHash() {
        return nuovaPasswordHash;
    }

    public void setNuovaPasswordHash(String nuovaPasswordHash) {
        this.nuovaPasswordHash = nuovaPasswordHash;
    }

    public String getVecchiaPasswordHash() {
        return vecchiaPasswordHash;
    }

    public void setVecchiaPasswordHash(String vecchiaPasswordHash) {
        this.vecchiaPasswordHash = vecchiaPasswordHash;
    }

    public String getNuovaBio() {
        return nuovaBio;
    }

    public void setNuovaBio(String nuovaBio) {
        this.nuovaBio = nuovaBio;
    }
}
