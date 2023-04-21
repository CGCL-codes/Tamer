    private static final void triFusion__(Node[] array, int deb, int fin, Node compare) throws Exception {
        if (deb != fin) {
            int milieu = (fin + deb) / 2;
            triFusion__(array, deb, milieu, compare);
            triFusion__(array, milieu + 1, fin, compare);
            fusion(array, deb, milieu, fin, compare);
        }
    }
