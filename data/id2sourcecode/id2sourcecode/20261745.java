    private Troncon choix_troncon(ArrayList<Double> ratp) {
        double rand = Math.random();
        int d = ratp.size() - 1;
        int i = 0;
        int g = 0;
        boolean trouve = false;
        while (!trouve) {
            i = (g + d) / 2;
            if (ratp.get(i) >= 0.0) {
                if (ratp.get(i) > rand) d = i - 1; else g = i + 1;
                trouve = (i > 0 && ratp.get(i) >= rand && ratp.get(i - 1) < rand) || (i == 0 && ratp.get(i) >= rand);
            } else {
                throw new IllegalArgumentException(ratp.get(i).toString());
            }
        }
        ratp.clear();
        return voisins.get(i);
    }
