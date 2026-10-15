package com.vigenere.Chivi_app.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;


import com.vigenere.Chivi_app.service.VigenereService;

@Controller
public class VigenereController {
	 private final VigenereService vigenereService;

	 public VigenereController(VigenereService vigenereService) {
	        this.vigenereService = vigenereService;
	    }

	    @GetMapping("/")
	    public String modeEtendu(Model model) {
	        model.addAttribute("mode", "etendu");
	        return "index";
	    }

	    @GetMapping("/classique")
	    public String modeClassique(Model model) {
	        model.addAttribute("mode", "classique");
	        return "index";
	    }

	    @PostMapping("/traiter")
	    public String traiter(@RequestParam String texte,
	                          @RequestParam String cle,
	                          @RequestParam String operation,
	                          @RequestParam String mode,
	                          Model model) {

	        model.addAttribute("texte", texte);
	        model.addAttribute("cle", cle);
	        model.addAttribute("operation", operation);
	        model.addAttribute("mode", mode);

	        try {
	            boolean dechiffrer = "dechiffrer".equals(operation);
	            String resultat;
	            if ("classique".equals(mode)) {
	                resultat = dechiffrer
	                        ? vigenereService.dechiffrerClassique(texte, cle)
	                        : vigenereService.chiffrerClassique(texte, cle);
	            } else {
	                resultat = dechiffrer
	                        ? vigenereService.dechiffrer(texte, cle)
	                        : vigenereService.chiffrer(texte, cle);
	            }
	            model.addAttribute("resultat", resultat);
	        } catch (IllegalArgumentException e) {
	            model.addAttribute("erreur", e.getMessage());
	        }

	        return "index";
	    }
	}
