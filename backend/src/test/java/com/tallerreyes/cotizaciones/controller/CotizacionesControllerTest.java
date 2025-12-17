package com.tallerreyes.cotizaciones.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class CotizacionesControllerTest {

    @GetMapping("/")
    public String home() {
        return "API Cotizaciones funcionando 🚀";
    }
}

