    private void createLand() throws FreeColException {
        int maxHeight = heightMap.getMax();
        int landThreshold = (maxHeight * landPercentage) / 100;
        int total = (width * height * landPercentage) / 100;
        int count;
        for (int i = 0; i < 100; i++) {
            count = heightMap.countLargerThan(landThreshold);
            if (Math.abs(total - count) > (5 * total / 100)) {
                if (count > total) {
                    landThreshold *= 11;
                } else {
                    landThreshold *= 9;
                }
                landThreshold /= 10;
            } else {
                break;
            }
        }
        total = (total * (hillsPercentage + mountainPercentage)) / 100;
        int hillThreshold = landThreshold + (maxHeight - landThreshold) / 4;
        for (int i = 0; i < 100; i++) {
            count = heightMap.countLargerThan(hillThreshold);
            if (Math.abs(total - count) > (total / 100)) {
                if (count > total) hillThreshold *= 11; else hillThreshold *= 9;
                hillThreshold /= 10;
            } else {
                break;
            }
        }
        total = (width * height * landPercentage * mountainPercentage) / 10000;
        int mountainThreshold = hillThreshold + (maxHeight - hillThreshold) / 2;
        for (int i = 0; i < 100; i++) {
            count = heightMap.countLargerThan(mountainThreshold);
            if (Math.abs(total - count) > (total / 100)) {
                if (count > total) {
                    mountainThreshold *= 11;
                } else {
                    mountainThreshold *= 9;
                }
                mountainThreshold /= 10;
            } else {
                break;
            }
        }
        Iterator iterator = map.getWholeMapIterator();
        while (iterator.hasNext()) {
            Position position = (Position) iterator.next();
            int thisHeight = heightMap.get(position);
            int terrainType;
            if (thisHeight < landThreshold) {
                terrainType = Tile.OCEAN;
                heightMap.set(position, 0);
            } else if (height < hillThreshold) {
                terrainType = Tile.GRASSLANDS;
            } else if (height < mountainThreshold) {
                terrainType = Tile.GRASSLANDS;
            } else {
                terrainType = Tile.GRASSLANDS;
            }
            Tile theTile = map.getTile(position);
            theTile.setType(terrainType);
        }
    }
