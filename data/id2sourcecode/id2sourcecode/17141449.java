    private static void makeClearing(Map m, int x, int y, boolean dec) {
        int w = Rand.d(8);
        int h = Rand.d(8);
        int x1 = x - w;
        int y1 = y - h;
        int x2 = x + w;
        int y2 = y + h;
        int cx = (x1 + x2) / 2;
        int cy = (y1 + y2) / 2;
        for (int lx = x1; lx <= (x1 + w * 2); lx++) for (int ly = y1; ly < (y1 + h * 2); ly++) {
            if ((((lx - cx) * (lx - cx) * 100) / (w * w) + ((ly - cy) * (ly - cy) * 100) / (h * h)) < 100) {
                if (m.getTile(lx, ly) == 0) m.setTile(lx, ly, m.floor());
            }
        }
        if (dec) if ((w >= 4) && (h >= 4) && (Rand.d(6) == 1) && (x != m.getEntrance().x)) {
            for (int i = 0; i < Rand.d(10); i++) {
                float a = RPG.random() * 100;
                int mx = (int) (0.5 + cx + 3 * Math.sin(a));
                int my = (int) (0.5 + cy + 3 * Math.cos(a));
                if (!m.isBlocked(mx, my)) m.addThing(Lib.create("mushroom"), mx, my);
            }
            for (int i = 0; i <= Rand.d(2, 4); i++) {
                Thing c = Lib.create("rabbit");
                c.set(RPG.ST_TARGETX, cx);
                c.set(RPG.ST_TARGETX, cy);
                m.addThing(c, cx, cy);
            }
        }
    }
