package com.example;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

public class FlightAnalysis {

    public static void main(String[] args) {
        String filePath = "tickets.json";
        try {
            String content = new String(Files.readAllBytes(Path.of(filePath)), "UTF-8");

            // Удаление BOM, если он есть
            if (content.startsWith("\uFEFF")) {
                content = content.substring(1);
            }

            JSONObject json = new JSONObject(content);
            JSONArray tickets = json.getJSONArray("tickets");

            Map<String, Duration> minFlightTimes = new HashMap<>();
            List<Double> prices = new ArrayList<>();
            double totalSum = 0;

            DateTimeFormatter dateFormatter = DateTimeFormatter.ofPattern("dd.MM.yy");
            DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("H:mm");

            ZoneId vvoZone = ZoneId.of("Asia/Vladivostok");
            ZoneId tlvZone = ZoneId.of("Asia/Jerusalem");


            for (int i = 0; i < tickets.length(); i++) {
                JSONObject ticket = tickets.getJSONObject(i);
                if (ticket.getString("origin").equals("VVO") && ticket.getString("destination").equals("TLV")) {
                    String carrier = ticket.getString("carrier");
                    LocalDate departureDate = LocalDate.parse(ticket.getString("departure_date"), dateFormatter);
                    LocalTime departureTime = LocalTime.parse(ticket.getString("departure_time"), timeFormatter);
                    ZonedDateTime departureDateTime = ZonedDateTime.of(departureDate, departureTime, vvoZone);

                    LocalDate arrivalDate = LocalDate.parse(ticket.getString("arrival_date"), dateFormatter);
                    LocalTime arrivalTime = LocalTime.parse(ticket.getString("arrival_time"), timeFormatter);
                    ZonedDateTime arrivalDateTime = ZonedDateTime.of(arrivalDate, arrivalTime, tlvZone);

                    Duration flightTime = Duration.between(departureDateTime, arrivalDateTime);
                    
                    minFlightTimes.put(carrier, minFlightTimes.getOrDefault(carrier, flightTime).compareTo(flightTime) < 0 ? minFlightTimes.get(carrier) : flightTime);
                    
                    double price = ticket.getDouble("price");
                    prices.add(price);
                    totalSum += price;
                }
            }

            double averagePrice = totalSum / prices.size();
            Collections.sort(prices);
            double medianPrice;
            if (prices.size() % 2 == 0) {
                medianPrice = (prices.get(prices.size() / 2 - 1) + prices.get(prices.size() / 2)) / 2.0;
            } else {
                medianPrice = prices.get(prices.size() / 2);
            }

            System.out.println("Минимальное время полета для каждого авиаперевозчика:");
            for (Map.Entry<String, Duration> entry : minFlightTimes.entrySet()) {
                System.out.println("Авиаперевозчик: " + entry.getKey() + ", Время полета: " + formatDuration(entry.getValue()));
            }

            System.out.println("Разница между средней ценой и медианой: " + Math.abs(averagePrice - medianPrice));

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static String formatDuration(Duration duration) {
        long hours = duration.toHours();
        long minutes = duration.toMinutes() % 60;
        return String.format("%d ч %d мин", hours, minutes);
    }
}

