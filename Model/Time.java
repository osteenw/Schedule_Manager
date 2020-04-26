package Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.time.LocalTime;

public class Time {
    int hour;
    int minutes;
    String dbTimeString;
    String displayString;

    public Time(int hour, int minutes, String dbTimeString, String displayString) {
        this.hour = hour;
        this.minutes = minutes;
        this.dbTimeString = dbTimeString;
        this.displayString = displayString;
    }

    public static ObservableList<Time> returnTime() {
        ObservableList<Time> timePicker = FXCollections.observableArrayList();
        for(int hour = 8; hour < 17; hour ++) {
            for(int minute = 0; minute < 60; minute += 15) {
                String displayString = "unknown";
                String dbTimeString = String.format("%02d", hour) + ":" + String.format("%02d", minute) + ":00";

                // Display String for 12AM
                if(hour == 0) {
                        displayString = "12:" + String.format("%02d", minute) + " AM";
                }
                if(hour > 0 && hour < 12) {
                        displayString = hour + ":" + String.format("%02d", minute) + " AM";
                }
                if(hour == 12) {
                        displayString = hour + ":" + String.format("%02d", minute) + " PM";
                }
                if(hour > 12) {
                        displayString = (hour - 12) + ":" + String.format("%02d", minute) + " PM";
                }

                timePicker.add(new Time(hour, minute, dbTimeString, displayString));
            }
        }

        return timePicker;
    }

    public static String amOrPm(LocalTime time) {
        int hour = time.getHour();
        int minute = time.getMinute();
        String displayString = null;

        // Display String for 12AM
        if(hour == 0) {
            displayString = "12:" + String.format("%02d", minute) + " AM";
        }
        if(hour > 0 && hour < 12) {
            displayString = hour + ":" + String.format("%02d", minute) + " AM";
        }
        if(hour == 12) {
            displayString = hour + ":" + String.format("%02d", minute) + " PM";
        }
        if(hour > 12) {
            displayString = (hour - 12) + ":" + String.format("%02d", minute) + " PM";
        }

        return displayString;
    }

    public int getHour() {
        return hour;
    }

    public void setHour(int hour) {
        this.hour = hour;
    }

    public int getMinutes() {
        return minutes;
    }

    public void setMinutes(int minutes) {
        this.minutes = minutes;
    }

    public String getDisplayString() {
        return displayString;
    }

    public void setDisplayString(String displayString) {
        this.displayString = displayString;
    }

    public String getDbTimeString() {
        return dbTimeString;
    }

    public void setDbTimeString(String dbTimeString) {
        this.dbTimeString = dbTimeString;
    }

    @Override
    public String toString() {
        return (displayString);
    }


}
