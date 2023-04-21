    public void init() {
        Random random = new Random();
        for (int i = 0; i < numPlayers; ++i) {
            int x;
            int y;
            do {
                x = 1 + random.nextInt(size - 1);
                y = 1 + random.nextInt(size - 1);
            } while (alreadyPicked(i, x, y));
            playerCoords[i] = new SectorCoords(x, y);
        }
    }
