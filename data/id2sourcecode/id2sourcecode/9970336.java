    public void paintShape(Component c, Graphics g, int x, int y) {
        int halfSize = getIconWidth() / 2;
        int yOffset = (halfSize + 1) / 2;
        g.fillPolygon(new int[] { x, x + halfSize, x + 2 * halfSize }, new int[] { y + yOffset, y + yOffset + halfSize, y + yOffset }, 3);
    }
