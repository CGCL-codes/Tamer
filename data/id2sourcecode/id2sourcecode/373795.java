    public static boolean etsiBin(int[] taulu, int etsi, int alku, int loppu) {
        int keskus = (alku + loppu) / 2;
        if (taulu[keskus] == etsi) {
            return true;
        } else if (keskus == alku && keskus == loppu) {
            return false;
        }
        if (taulu[keskus] > etsi) {
            return etsiBin(taulu, etsi, alku, keskus);
        } else {
            return etsiBin(taulu, etsi, keskus, loppu);
        }
    }
