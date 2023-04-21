package net.medienablage.domain;

import java.util.Comparator;

public class MedienComparator implements Comparator<Medium> {

    public int compare(Medium medium1, Medium medium2) {
        if (medium1.getClass().equals(medium2.getClass()) && medium1.getTitel() != null && medium2.getTitel() != null) {
            int toReturn = medium1.getTitel().compareTo(medium2.getTitel());
            return toReturn == 0 ? medium1.getJahr() - medium2.getJahr() : toReturn;
        }
        return -1;
    }
}
