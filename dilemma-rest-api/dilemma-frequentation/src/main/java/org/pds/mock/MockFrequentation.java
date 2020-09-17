package org.pds.mock;


import org.pds.model.Frequentation;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

public class MockFrequentation {

    public MockFrequentation() {
    }

    public Frequentation insertFrequentations() {
        System.out.println("start insert !");
        RandomDate randomDates = new RandomDate();
        String[] randomStrings = {"Entree", "Sortie"};
        String[] randomShop = {"Darty", "Fnac", "zara", "GO sport", "Micromania", "H&M", "Pull and Bear", "Minelli", "Andre", "Sephora"};
        System.out.println("Inserting a new Frequentation...");
        int client = randomDates.createRandomIntBetween(1, 70);
        Random random1 = new Random();
        String Es = randomStrings[random1.nextInt(randomStrings.length)];
        Random random2 = new Random();
        String shop = randomShop[random2.nextInt(randomShop.length)];
        LocalDateTime theDate = randomDates.createRandomDate(2020, 2021);
        Frequentation frequentation = new Frequentation(null,Integer.toString(client),shop,Es, null);
        frequentation.setDate(theDate.format(DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss")));

        return frequentation;
    }
}
