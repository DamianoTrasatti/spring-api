package com.example.api;

import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
public class PostController {

    // Crea la lista per contenere i post
    private List<Post> listaPost = new ArrayList<>();

    // Crea un post e lo mette nella lista metodo: POST
    @PostMapping("/crea_post")
    public String creaPost(@RequestBody Post nuovoPost) {
        listaPost.add(nuovoPost);
        return "Post creato con successo per " + nuovoPost.getNomeUtente();
    }

    // Ottiene tutti i post contenuti nella lista metodo: GET
    @GetMapping("/post")
    public List<Post> getTuttiIPost() {
        return listaPost;
    }

    // Cancella un post specifico metodo: DELETE
    @DeleteMapping("/elimina_post")
    public String eliminaPost(@RequestBody Post postDaEliminare) {
        boolean rimosso = listaPost.removeIf(post ->
            post.getNomeUtente().equals(postDaEliminare.getNomeUtente()) &&
            post.getLuogo().equals(postDaEliminare.getLuogo()) &&
            post.getContenuto().equals(postDaEliminare.getContenuto())
        );

        if (rimosso) {
            String nome = postDaEliminare.getNomeUtente();
            return "Post eliminato con successo per " + nome;
        } else {
            return "Nessun post trovato con quei dati.";
        }
    }
}
