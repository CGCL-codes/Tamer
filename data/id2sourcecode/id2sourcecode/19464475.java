    private Coordinate search(int x1, int y1, int x2, int y2, int key) {
        if (x2 < x1 || y2 < y1 || x1 * y1 > key || x2 * y2 < key) return null;
        if (x1 * y1 == key) return new Coordinate(x1, y1);
        if (x2 * y2 == key) return new Coordinate(x2, y2);
        int x = (x1 + x2) / 2;
        int y = (y1 + y2) / 2;
        if (x * y == key) return new Coordinate(x, y); else if (x * y > key) return search(x1, y1, x, y, key); else {
            if (x + 1 <= n) {
                Coordinate co = search(x + 1, y1, x2, y2, key);
                if (co != null) return co;
            }
            if (y + 1 <= m) return search(x1, y + 1, x, y2, key);
        }
        return null;
    }
