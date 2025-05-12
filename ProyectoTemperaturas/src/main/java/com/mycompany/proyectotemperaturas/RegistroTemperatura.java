/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.proyectotemperaturas;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 *
 * @author bonni
 */
public class RegistroTemperatura {
    private String ciudad;
    private LocalDate fecha;
    private double temperatura;

    public RegistroTemperatura(String ciudad, LocalDate fecha, double temperatura) {
        this.ciudad = ciudad;
        this.fecha = fecha;
        this.temperatura = temperatura;
    }

    public RegistroTemperatura(String ciudad, String fechaStr, String tempStr) {
        this.ciudad = ciudad;
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");
        this.fecha = LocalDate.parse(fechaStr, formatter); // ← aquí usamos la variable correcta 🛠️
        this.temperatura = Double.parseDouble(tempStr);
    }

    public String getCiudad() {
        return ciudad;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public double getTemperatura() {
        return temperatura;
    }

    @Override
    public String toString() {
        return ciudad + " | " + fecha + " | " + temperatura + "°C";
    }
}
