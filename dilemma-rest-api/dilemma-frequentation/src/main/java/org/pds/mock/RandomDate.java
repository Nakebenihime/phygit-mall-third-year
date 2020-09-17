package org.pds.mock;

import java.time.LocalDateTime;

public class RandomDate {
    public int createRandomIntBetween(int start, int end) {
        return start + (int) Math.round(Math.random() * (end - start));
    }

    public LocalDateTime createRandomDate(int startYear, int endYear) {
        int day = createRandomIntBetween(1, 28);
        int month = createRandomIntBetween(1, 12);
        int year = createRandomIntBetween(startYear, endYear);
        int heure = createRandomIntBetween(0, 23);
        int minute = createRandomIntBetween(0, 59);
        int sec = createRandomIntBetween(0, 59);
        return LocalDateTime.of(year, month, day,heure,minute,sec);
    }
}
