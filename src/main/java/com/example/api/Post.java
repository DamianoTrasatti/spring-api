package com.example.api;


public class Post {
    private String nomeUtente;
    private String luogo;
    private String contenuto;

    public Post() {}

    public Post(String nomeUtente, String luogo, String contenuto) {
        this.nomeUtente = nomeUtente;
        this.luogo = luogo;
        this.contenuto = contenuto;
    }

    // Valori vari e ricezioni
    /*
        Set serve se vogliamo modificare qualcosa dopo aver creato l'oggetto o dopo aver preso il valore con Get
        Get serve per prendere il valore dall'input tipo del body ad esempio come abbiamo fatto 
    */
    public String getNomeUtente() {
        return nomeUtente;
    }

    public void setNomeUtente(String nomeUtente) {
        this.nomeUtente = nomeUtente;
    }

    public String getLuogo() {
        return luogo;
    }

    public void setLuogo(String luogo) {
        this.luogo = luogo;
    }

    public String getContenuto() {
        return contenuto;
    }

    public void setContenuto(String contenuto) {
        this.contenuto = contenuto;
    }
}
