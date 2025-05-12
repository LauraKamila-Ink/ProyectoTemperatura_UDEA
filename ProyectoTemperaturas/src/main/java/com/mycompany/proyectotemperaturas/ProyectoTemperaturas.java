/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.proyectotemperaturas;

/**
 *
 * @author bonni
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Map;
import java.util.Comparator;
import java.util.Scanner;
import java.util.stream.Collectors;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import javax.swing.JFrame;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.data.category.DefaultCategoryDataset;

public class ProyectoTemperaturas {

    public static void main(String[] args) {
        List<RegistroTemperatura> registros = cargarCSV();
        mostrarPromedioPorRangoDeFechas(registros);
        ciudadMasCalurosaYFriaPorFecha(registros);
    }

    public static List<RegistroTemperatura> cargarCSV() {
        List<RegistroTemperatura> lista = new ArrayList<>();
        String archivo = "src/main/resources/Temperaturas.csv";  // Asegúrate de que el archivo esté allí

        try (BufferedReader br = new BufferedReader(new FileReader(archivo))) {
            String linea;
            br.readLine(); // Saltar encabezado

            while ((linea = br.readLine()) != null) {
                String[] datos = linea.split(",");
                RegistroTemperatura registro = new RegistroTemperatura(datos[0], datos[1], datos[2]);
                lista.add(registro);
            }
        } catch (IOException e) {
            System.err.println("Error al leer el archivo: " + e.getMessage());
        }
        return lista;
    }

    public static void mostrarPromedioPorRangoDeFechas(List<RegistroTemperatura> registros) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.print("Ingrese la fecha de inicio (dd/MM/yyyy): ");
        LocalDate inicio = LocalDate.parse(scanner.nextLine(), formatter);

        System.out.print("Ingrese la fecha de fin (dd/MM/yyyy): ");
        LocalDate fin = LocalDate.parse(scanner.nextLine(), formatter);

        Map<String, Double> promedios = registros.stream()
                .filter(r -> !r.getFecha().isBefore(inicio) && !r.getFecha().isAfter(fin))
                .collect(Collectors.groupingBy(
                        RegistroTemperatura::getCiudad,
                        Collectors.averagingDouble(RegistroTemperatura::getTemperatura)
                ));

        System.out.println("\nPromedio de temperaturas por ciudad:");
        promedios.forEach((ciudad, promedio) -> 
                System.out.printf("%s: %.2f°C\n", ciudad, promedio));
        
        mostrarGrafico(promedios); // Mostrar gráfico
    }

    public static void ciudadMasCalurosaYFriaPorFecha(List<RegistroTemperatura> registros) {
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy");

        System.out.print("Ingrese una fecha específica (dd/MM/yyyy): ");
        LocalDate fechaIngresada = LocalDate.parse(scanner.nextLine(), formatter);

        List<RegistroTemperatura> registrosFiltrados = registros.stream()
                .filter(r -> r.getFecha().equals(fechaIngresada))
                .toList();

        if (registrosFiltrados.isEmpty()) {
            System.out.println("No hay registros para esa fecha.");
            return;
        }

        RegistroTemperatura masCalurosa = registrosFiltrados.stream()
                .max(Comparator.comparingDouble(RegistroTemperatura::getTemperatura))
                .get();

        RegistroTemperatura masFria = registrosFiltrados.stream()
                .min(Comparator.comparingDouble(RegistroTemperatura::getTemperatura))
                .get();

        System.out.println("\nResultados para la fecha " + fechaIngresada + ":");
        System.out.println("Ciudad más calurosa: " + masCalurosa.getCiudad() + " (" + masCalurosa.getTemperatura() + "°C)");
        System.out.println("Ciudad más fría: " + masFria.getCiudad() + " (" + masFria.getTemperatura() + "°C)");
    }

    public static void mostrarGrafico(Map<String, Double> promedios) {
        DefaultCategoryDataset dataset = new DefaultCategoryDataset();

        promedios.forEach((ciudad, promedio) ->
                dataset.addValue(promedio, "Promedio", ciudad));

        JFreeChart chart = ChartFactory.createBarChart(
                "Promedio de Temperaturas por Ciudad",
                "Ciudad",
                "Temperatura (°C)",
                dataset,
                PlotOrientation.VERTICAL,
                true, true, false
        );

        // Mostrar el gráfico en una ventana
        ChartPanel panel = new ChartPanel(chart);
        panel.setPreferredSize(new java.awt.Dimension(800, 600));

        JFrame frame = new JFrame("Gráfico de Promedios");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setContentPane(panel); // 💫 ¡ESTA línea era la que faltaba!
        frame.pack();
        frame.setLocationRelativeTo(null); // Centrar ventana
        frame.setVisible(true);
    }
}
