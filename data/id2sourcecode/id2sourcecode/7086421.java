    void findInLine(QuadTree a, int x, int y, QuadTree b, int xx, int yy, List<HasXY> items) {
        if (a.equals(b)) {
            return;
        }
        items.addAll(a.contained);
        items.addAll(b.contained);
        int mx = (x + xx) / 2;
        int my = (y + yy) / 2;
        QuadTree c = whichChild(mx, my);
        findInLine(a, x, y, c, mx, my, items);
        findInLine(b, xx, yy, c, mx, my, items);
    }
