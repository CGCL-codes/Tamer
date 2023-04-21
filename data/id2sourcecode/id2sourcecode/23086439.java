    private static final int findIgnoreCase(MapElement[] map, CharChunk name, int start, int end) {
        int a = 0;
        int b = map.length - 1;
        if (b == -1) {
            return -1;
        }
        if (compareIgnoreCase(name, start, end, map[0].name) < 0) {
            return -1;
        }
        if (b == 0) {
            return 0;
        }
        int i = 0;
        while (true) {
            i = (b + a) / 2;
            int result = compareIgnoreCase(name, start, end, map[i].name);
            if (result == 1) {
                a = i;
            } else if (result == 0) {
                return i;
            } else {
                b = i;
            }
            if ((b - a) == 1) {
                int result2 = compareIgnoreCase(name, start, end, map[b].name);
                if (result2 < 0) {
                    return a;
                } else {
                    return b;
                }
            }
        }
    }
