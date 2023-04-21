    protected Rectangle boundingRect(int x, int y, int oldX, int oldY) {
        int dx = Math.abs(oldX - x) / 2;
        int dy = Math.abs(oldY - y) / 2;
        int xOffset = dx + SCALE * SZ;
        int yOffset = dy + SCALE * SZ;
        int xCenter = (x + oldX) / 2;
        int yCenter = (y + oldY) / 2;
        int xOrg = Math.max(xCenter - xOffset, 0);
        int yOrg = Math.max(yCenter - yOffset, 0);
        int w = 2 * xOffset;
        int h = 2 * yOffset;
        return new Rectangle(xOrg, yOrg, w, h);
    }
