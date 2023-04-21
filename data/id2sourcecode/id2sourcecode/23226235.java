    public static boolean makeOvalRoom(Map m, int x, int y, int dx, int dy) {
        int w = Rand.d(2, 3);
        int h = Rand.d(2, 3);
        int x1 = x + (dx - 1) * w;
        int y1 = y + (dy - 1) * h;
        int x2 = x + (dx + 1) * w;
        int y2 = y + (dy + 1) * h;
        if (!m.isBlank(x1, y1, x2, y2)) return false;
        int cx = (x1 + x2) / 2;
        int cy = (y1 + y2) / 2;
        for (int lx = x1; lx <= (x1 + w * 2); lx++) for (int ly = y1; ly < (y1 + h * 2); ly++) {
            if ((((lx - cx) * (lx - cx) * 100) / (w * w) + ((ly - cy) * (ly - cy) * 100) / (h * h)) < 100) m.setTile(lx, ly, m.floor());
        }
        m.fillArea(cx, cy, x, y, m.floor());
        return true;
    }
