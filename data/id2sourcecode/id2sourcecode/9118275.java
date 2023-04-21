    private ArrayList processBuilding(int[][] building, int minArea, Random rand) {
        int a = RescueMapToolkit.area(building);
        if (a < 1000) return new ArrayList(0);
        if (a < minArea) {
            ArrayList l = new ArrayList(1);
            l.add(building);
            return l;
        }
        int lower = (int) (rand.nextDouble() * minArea);
        lower = lower * 4;
        if (a < lower) {
            ArrayList l = new ArrayList(1);
            l.add(building);
            return l;
        }
        int minX = building[0][0];
        int minY = building[0][1];
        int maxX = building[0][0];
        int maxY = building[0][1];
        for (int i = 1; i < building.length; i++) {
            if (minX > building[i][0]) minX = building[i][0];
            if (maxX < building[i][0]) maxX = building[i][0];
            if (minY > building[i][1]) minY = building[i][1];
            if (maxY < building[i][1]) maxY = building[i][1];
        }
        int midX = (minX + maxX) / 2;
        int midY = (minY + maxY) / 2;
        int[][][] split;
        if (maxX - minX > maxY - minY) split = RescueMapToolkit.split(building, midX, minY, midX, maxY); else split = RescueMapToolkit.split(building, minX, midY, maxX, midY);
        if (split == null || RescueMapToolkit.area(split[0]) == 0 || RescueMapToolkit.area(split[1]) == 0) return new ArrayList(0);
        ArrayList a1 = processBuilding(split[0], minArea, rand);
        ArrayList a2 = processBuilding(split[1], minArea, rand);
        ArrayList toRet = new ArrayList(a1.size() + a2.size());
        for (int i = 0; i < a1.size(); i++) toRet.add(a1.get(i));
        for (int i = 0; i < a2.size(); i++) toRet.add(a2.get(i));
        return toRet;
    }
