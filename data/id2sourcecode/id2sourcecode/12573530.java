    private void drawLabel(Graphics graphics, Point location, String string) {
        FontMetrics metrics = graphics.getFontMetrics();
        int stringWidth = metrics.stringWidth(string);
        int stringHeight = metrics.getAscent();
        int x = Math.max((m_fieldSize - stringWidth) / 2, 0);
        int y = stringHeight + (m_fieldSize - stringHeight) / 2;
        graphics.drawString(string, location.x + x, location.y + y);
    }
