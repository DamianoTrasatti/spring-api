package com.example.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

@RestController
public class UserController {

    private static final String DB_URL = "jdbc:postgresql://dpg-d17uh7h5pdvs738r4d2g-a.oregon-postgres.render.com:5432/streetart_tour";
    private static final String DB_USER = "root";
    private static final String DB_PASSWORD = "7qNoSJjf1FgqPQ6goxJEFWsMqCQ4adRL";

    @GetMapping("/utenti")
    public List<Utenti> getTuttiGliUtenti() {
        List<Utenti> utenti = new ArrayList<>();
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery("SELECT id, nome, cognome, username, email, password_hash, bio FROM users");

            while (rs.next()) {
                Utenti u = new Utenti();
                u.setId(rs.getString("id"));
                u.setNome(rs.getString("nome"));
                u.setCognome(rs.getString("cognome"));
                u.setUsername(rs.getString("username"));
                u.setEmail(rs.getString("email"));
                u.setPasswordHash(rs.getString("password_hash"));
                u.setBio(rs.getString("bio"));

                utenti.add(u);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return utenti;
    }

    @PostMapping("/crea_utenti")
    public String creaUtente(@RequestBody Utenti nuovoUtente) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "INSERT INTO users (nome, cognome, username, email, password_hash, bio) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, nuovoUtente.getNome());
            ps.setString(2, nuovoUtente.getCognome());
            ps.setString(3, nuovoUtente.getUsername());
            ps.setString(4, nuovoUtente.getEmail());
            ps.setString(5, nuovoUtente.getPasswordHash());
            ps.setString(6, nuovoUtente.getBio());

            ps.executeUpdate();
            return "Utente creato con successo per " + nuovoUtente.getNome();
        } catch (SQLException e) {
            e.printStackTrace();
            return "Errore nella creazione utente.";
        }
    }

    @PutMapping("/modifica_utenti")
    public String modificaNomeUtente(@RequestBody ModificaNomeRequest richiesta) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE users SET nome = ?, cognome = ?, username = ?, email = ?, password_hash = ?, bio = ? WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, richiesta.getNuovoNome());
            ps.setString(2, richiesta.getNuovoCognome());
            ps.setString(3, richiesta.getNuovoUsername());
            ps.setString(4, richiesta.getNuovaEmail());
            ps.setString(5, richiesta.getNuovaPasswordHash());
            ps.setString(6, richiesta.getNuovaBio());
            ps.setString(7, richiesta.getVecchioUsername());

            int updated = ps.executeUpdate();
            if (updated > 0) {
                return "Utente aggiornato con successo.";
            } else {
                return "Utente non trovato.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Errore nella modifica.";
        }
    }

    @DeleteMapping("/elimina_utenti")
    public String eliminaUtenti(@RequestBody Utenti utenteDaEliminare) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM users WHERE nome = ? AND cognome = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, utenteDaEliminare.getNome());
            ps.setString(2, utenteDaEliminare.getCognome());
            int deleted = ps.executeUpdate();

            if (deleted > 0) {
                return "Utente eliminato con successo per " + utenteDaEliminare.getNome();
            } else {
                return "Utente non trovato.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Errore nell'eliminazione.";
        }
    }

    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody LoginRequest request) {
        String sql = "SELECT password_hash FROM users WHERE username = ?";
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
            PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, request.getUsername());
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                String storedHash = rs.getString("password_hash");

                // Verifica la password inserita con quella salvata
                // BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
                // if (encoder.matches(request.getPassword(), storedHash)) {
                if (request.getPassword().equals(storedHash)) {
                    return ResponseEntity.ok("Login avvenuto con successo");
                } else {
                    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Password errata");
                }
            } else {
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Utente non trovato");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore durante il login");
        }
    }

}
