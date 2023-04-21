    public int isHandle(int sx, int sy) {
        if (clipboard != null || ic == null) return -1;
        double mag = ic.getMagnification();
        int size = HANDLE_SIZE + 3;
        int halfSize = size / 2;
        int sx1 = ic.screenX(x) - halfSize;
        int sy1 = ic.screenY(y) - halfSize;
        int sx3 = ic.screenX(x + width) - halfSize;
        int sy3 = ic.screenY(y + height) - halfSize;
        int sx2 = sx1 + (sx3 - sx1) / 2;
        int sy2 = sy1 + (sy3 - sy1) / 2;
        if (sx >= sx1 && sx <= sx1 + size && sy >= sy1 && sy <= sy1 + size) return 0;
        if (sx >= sx2 && sx <= sx2 + size && sy >= sy1 && sy <= sy1 + size) return 1;
        if (sx >= sx3 && sx <= sx3 + size && sy >= sy1 && sy <= sy1 + size) return 2;
        if (sx >= sx3 && sx <= sx3 + size && sy >= sy2 && sy <= sy2 + size) return 3;
        if (sx >= sx3 && sx <= sx3 + size && sy >= sy3 && sy <= sy3 + size) return 4;
        if (sx >= sx2 && sx <= sx2 + size && sy >= sy3 && sy <= sy3 + size) return 5;
        if (sx >= sx1 && sx <= sx1 + size && sy >= sy3 && sy <= sy3 + size) return 6;
        if (sx >= sx1 && sx <= sx1 + size && sy >= sy2 && sy <= sy2 + size) return 7;
        return -1;
    }
