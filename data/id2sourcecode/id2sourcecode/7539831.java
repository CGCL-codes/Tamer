    protected void divideGrid(Graphics g, float x, float y, float width, float height, float a0, float a1, float a2, float a3) {
        float edge1, edge2, edge3, edge4, middle;
        float newWidth = width / 2;
        float newHeight = height / 2;
        if (width > 2 || height > 2) {
            middle = (a0 + a1 + a2 + a3) / 4 + displace(width, height, newWidth + newHeight);
            edge1 = (a0 + a1) / 2;
            edge2 = (a1 + a2) / 2;
            edge3 = (a2 + a3) / 2;
            edge4 = (a3 + a1) / 2;
            if (middle < 0) {
                middle = 0;
            } else if (middle > 1.0f) {
                middle = 1.0f;
            }
            divideGrid(g, x, y, newWidth, newHeight, a0, edge1, middle, edge4);
            divideGrid(g, x + newWidth, y, newWidth, newHeight, edge1, a1, edge2, middle);
            divideGrid(g, x + newWidth, y + newHeight, newWidth, newHeight, middle, edge2, a2, edge3);
            divideGrid(g, x, y + newHeight, newWidth, newHeight, edge4, middle, edge3, a3);
        } else {
            g.setColor(computeColor((a0 + a1 + a2 + a3) / 4));
            g.drawRect((int) x, (int) y, 1, 1);
        }
    }
