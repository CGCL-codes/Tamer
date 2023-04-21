    public void spray(int x1, int y1, int x2, int y2, int c, int density) {
        int cx = (x2 + y1) / 2;
        int cy = (y2 + y1) / 2;
        setTile(cx, cy, c);
        int w = x2 - x1 + 1;
        int h = y2 - y1 + 1;
        for (int i = ((w + h) * density) / 100; i > 0; i--) {
            int x = RPG.rspread(x1, x2);
            int y = RPG.rspread(y1, y2);
            makeRandomPath(cx, cy, x, y, x1, y1, x2, y2, c, false);
        }
    }
