    public Tile useTile(int i) {
        if ((tray[i] == null) || (i >= TRAY_SIZE) || (i < 0)) return null;
        Tile t = tray[i];
        for (int j = i; j < getNumUnusedTiles() - 1; j++) tray[j] = tray[j + 1];
        tray[getNumUnusedTiles() - 1] = null;
        setNumUnusedTiles(getNumUnusedTiles() - 1);
        return t;
    }
