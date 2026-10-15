package com.vigenere.Chivi_app.service;

import java.text.Normalizer;

import org.springframework.stereotype.Service;

@Service
public class VigenereService {

    // Alphabet étendu : majuscules, minuscules, chiffres, lettres accentuées
    private static final String ALPHABET =
            "ABCDEFGHIJKLMNOPQRSTUVWXYZ"
          + "abcdefghijklmnopqrstuvwxyz"
          + "0123456789"
          + "àâäçéèêëîïôöùûüÿœæ"
          + "ÀÂÄÇÉÈÊËÎÏÔÖÙÛÜŸŒÆ";

    private static final int TAILLE = ALPHABET.length();

    public String chiffrer(String texte, String cle) {
        return traiter(texte, cle, 1);
    }

    public String dechiffrer(String texte, String cle) {
        return traiter(texte, cle, -1);
    }

    private String traiter(String texte, String cle, int sens) {
        if (texte == null) {
            texte = "";
        }
        if (cle == null || cle.isEmpty()) {
            throw new IllegalArgumentException("La clé ne doit pas être vide.");
        }

        // Uniformise les accents (é écrit en un seul caractère)
        texte = Normalizer.normalize(texte, Normalizer.Form.NFC);
        cle = Normalizer.normalize(cle, Normalizer.Form.NFC);

        // Convertit la clé en décalages
        int[] decalages = new int[cle.length()];
        for (int i = 0; i < cle.length(); i++) {
            int pos = ALPHABET.indexOf(cle.charAt(i));
            if (pos < 0) {
                throw new IllegalArgumentException(
                        "Caractère non autorisé dans la clé : '" + cle.charAt(i) + "'");
            }
            decalages[i] = pos;
        }

        StringBuilder resultat = new StringBuilder();
        int indexCle = 0;

        for (char c : texte.toCharArray()) {
            int pos = ALPHABET.indexOf(c);
            if (pos < 0) {
                // Espace, ponctuation... : laissé tel quel, la clé n'avance pas
                resultat.append(c);
            } else {
                int decalage = decalages[indexCle % decalages.length];
                int nouvellePos = Math.floorMod(pos + sens * decalage, TAILLE);
                resultat.append(ALPHABET.charAt(nouvellePos));
                indexCle++;
            }
        }
        return resultat.toString();
    }
    // ===== MODE CLASSIQUE : alphabet A-Z uniquement =====

    public String chiffrerClassique(String texte, String cle) {
        return traiterClassique(texte, cle, 1);
    }

    public String dechiffrerClassique(String texte, String cle) {
        return traiterClassique(texte, cle, -1);
    }

    private String traiterClassique(String texte, String cle, int sens) {
        if (texte == null) {
            texte = "";
        }
        if (cle == null || cle.isEmpty()) {
            throw new IllegalArgumentException("La clé ne doit pas être vide.");
        }

        // La clé ne peut contenir que les lettres A-Z (sans accent)
        StringBuilder cleMajuscule = new StringBuilder();
        for (char c : cle.toCharArray()) {
            if ((c >= 'A' && c <= 'Z') || (c >= 'a' && c <= 'z')) {
                cleMajuscule.append(Character.toUpperCase(c));
            } else {
                throw new IllegalArgumentException(
                        "En mode classique, la clé doit contenir uniquement des lettres de A à Z (sans accent, chiffre ni espace).");
            }
        }

        StringBuilder resultat = new StringBuilder();
        int indexCle = 0;

        for (char c : texte.toCharArray()) {
            char base;
            if (c >= 'A' && c <= 'Z') {
                base = 'A';
            } else if (c >= 'a' && c <= 'z') {
                base = 'a';
            } else {
                // Chiffres, accents, espaces, ponctuation : inchangés
                resultat.append(c);
                continue;
            }
            int decalage = cleMajuscule.charAt(indexCle % cleMajuscule.length()) - 'A';
            int pos = Math.floorMod(c - base + sens * decalage, 26);
            resultat.append((char) (base + pos));
            indexCle++;
        }
        return resultat.toString();
    }    
}
