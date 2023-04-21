    private Group layoutPlayers(int numPlayers, int numUnits, TerrainModel terrain) {
        final int margin = 50;
        final int separation = 30;
        int left = margin;
        int right = terrain.getWidth() - margin;
        int top = margin;
        int bottom = terrain.getHeight() - margin;
        int centre = (left + right) / 2;
        int mid = (top + bottom) / 2;
        Group result = new Group();
        result.add(new Resource(100, 300, 0, 20, 1000, terrain));
        if (numPlayers > 0) layoutUnits(0, numUnits, left, top, separation, separation, result, terrain);
        if (numPlayers > 1) layoutUnits(1, numUnits, right, bottom, -separation, -separation, result, terrain);
        if (numPlayers > 2) layoutUnits(2, numUnits, right, top, -separation, separation, result, terrain);
        if (numPlayers > 3) layoutUnits(3, numUnits, left, bottom, separation, -separation, result, terrain);
        if (numPlayers > 4) layoutUnits(4, numUnits, centre, top, 0, separation, result, terrain);
        if (numPlayers > 5) layoutUnits(5, numUnits, centre, bottom, 0, -separation, result, terrain);
        if (numPlayers > 6) layoutUnits(6, numUnits, right, mid, -separation, 0, result, terrain);
        if (numPlayers > 7) layoutUnits(7, numUnits, left, mid, separation, 0, result, terrain);
        return result;
    }
