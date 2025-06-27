package com.example.api;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
// import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

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
    public ResponseEntity<String> creaUtente(@RequestBody Utenti nuovoUtente) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {

            // Controlla se username esiste
            String checkUsernameSql = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement checkUsernameStmt = conn.prepareStatement(checkUsernameSql)) {
                checkUsernameStmt.setString(1, nuovoUtente.getUsername());
                ResultSet rs = checkUsernameStmt.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Username già esistente.");
                }
            }

            // Controlla se email esiste
            String checkEmailSql = "SELECT COUNT(*) FROM users WHERE email = ?";
            try (PreparedStatement checkEmailStmt = conn.prepareStatement(checkEmailSql)) {
                checkEmailStmt.setString(1, nuovoUtente.getEmail());
                ResultSet rs = checkEmailStmt.executeQuery();
                rs.next();
                if (rs.getInt(1) > 0) {
                    return ResponseEntity.status(HttpStatus.CONFLICT).body("Email già esistente.");
                }
            }

            // Inserisci nuovo utente
            String insertSql = "INSERT INTO users (nome, cognome, username, email, password_hash, bio) VALUES (?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = conn.prepareStatement(insertSql)) {
                insertStmt.setString(1, nuovoUtente.getNome());
                insertStmt.setString(2, nuovoUtente.getCognome());
                insertStmt.setString(3, nuovoUtente.getUsername());
                insertStmt.setString(4, nuovoUtente.getEmail());
                insertStmt.setString(5, nuovoUtente.getPasswordHash());
                insertStmt.setString(6, nuovoUtente.getBio());

                insertStmt.executeUpdate();
                return ResponseEntity.ok("Utente creato con successo per " + nuovoUtente.getNome());
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Errore nella creazione utente.");
        }
    }


    @PutMapping("/modifica_nomecognome_utenti")
    public String modificaNomeCognomeUtente(@RequestBody ModificaUtenteRequest richiesta) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE users SET nome = ?, cognome = ? WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, richiesta.getNuovoNome());
            ps.setString(2, richiesta.getNuovoCognome());
            ps.setString(3, richiesta.getVecchioUsername());

            int updated = ps.executeUpdate();
            if (updated > 0) {
                return "Name/surname changed.";
            } else {
                return "User not found.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error.";
        }
    }


    @PutMapping("/modifica_username_utenti")
    public String modificaUsernameUtente(@RequestBody ModificaUtenteRequest richiesta) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            
            // 1. Verifica se il nuovo username è già esistente
            String checkSql = "SELECT COUNT(*) FROM users WHERE username = ?";
            try (PreparedStatement checkStmt = conn.prepareStatement(checkSql)) {
                checkStmt.setString(1, richiesta.getNuovoUsername());
                try (ResultSet rs = checkStmt.executeQuery()) {
                    if (rs.next() && rs.getInt(1) > 0) {
                        return "Username already exist.";
                    }
                }
            }

            // 2. Esegui l'aggiornamento se l'username è disponibile
            String updateSql = "UPDATE users SET username = ? WHERE username = ?";
            try (PreparedStatement ps = conn.prepareStatement(updateSql)) {
                ps.setString(1, richiesta.getNuovoUsername());
                ps.setString(2, richiesta.getVecchioUsername());

                int updated = ps.executeUpdate();
                if (updated > 0) {
                    return "Username changed.";
                } else {
                    return "User not found.";
                }
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return "Error.";
        }
    }   


    @PutMapping("/modifica_password_utenti")
    public String modificaPasswordUtente(@RequestBody ModificaUtenteRequest richiesta) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "UPDATE users SET password_hash = ? WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, richiesta.getNuovaPasswordHash());
            ps.setString(2, richiesta.getVecchioUsername());

            int updated = ps.executeUpdate();
            if (updated > 0) {
                return "Pasword changed!";
            } else {
                return "User not found!.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error!";
        }
    }


    @DeleteMapping("/elimina_utenti")
    public String eliminaUtenti(@RequestBody ModificaUtenteRequest utenteDaEliminare) {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String sql = "DELETE FROM users WHERE username = ? AND password_hash = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, utenteDaEliminare.getVecchioUsername());
            ps.setString(2, utenteDaEliminare.getVecchiaPasswordHash());
            int deleted = ps.executeUpdate();

            if (deleted > 0) {
                return "User deleted";
            } else {
                return "User not found.";
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return "Error.";
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
                if (request.getPasswordHash().equals(storedHash)) {
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
