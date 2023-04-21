    private static void paintArrow(final Graphics g, final int x, final int y, final int direction, final int size, final boolean wide, final Color color, final boolean fill) {
        final int halfHeight = (size + 1) / 2;
        final int height = halfHeight * 2;
        final int width = wide ? halfHeight : height;
        final int[] heights = new int[] { 0, halfHeight - 1, height - 2 };
        final int[] lWidths = new int[] { width - 1, 0, width - 1 };
        final int[] rWidths = new int[] { 0, width - 1, 0 };
        int[] px = null;
        int[] py = null;
        switch(direction) {
            case NORTH:
                px = heights;
                py = lWidths;
                break;
            case SOUTH:
                px = heights;
                py = rWidths;
                break;
            case WEST:
            case LEFT:
                px = lWidths;
                py = heights;
                break;
            case EAST:
            case RIGHT:
                px = rWidths;
                py = heights;
                break;
            default:
                return;
        }
        final Color oldColor = g.getColor();
        g.setColor(color);
        g.translate(x, y);
        g.drawPolygon(px, py, 3);
        if (fill) {
            g.fillPolygon(px, py, 3);
        }
        g.translate(-x, -y);
        g.setColor(oldColor);
    }
