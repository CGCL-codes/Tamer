    private int[] decaleMiniListe(int[] liste) {
        if (liste.length != 9) System.exit(45);
        int temp;
        for (int i = 0; i < 8; i++) {
            temp = liste[i];
            liste[i] = liste[i + 1];
            liste[i + 1] = temp;
        }
        return liste;
    }
