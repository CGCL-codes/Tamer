    public boolean isNear(Point point) {
        int x = point.x - getX();
        int y = point.y - getY();
        if (type == ZIGZAG) {
            int midY = (srcY + destY) / 2;
            return isNearLine(srcX, srcY, srcX, midY, x, y) || isNearLine(srcX, midY, destX, midY, x, y) || isNearLine(destX, midY, destX, destY, x, y);
        } else if (type == LINE) {
            return isNearLine(srcX, srcY, destX, destY, x, y);
        } else if (type == CURVE) {
            return path.intersects(x - 2, y - 2, 4, 4);
        } else {
            return false;
        }
    }
