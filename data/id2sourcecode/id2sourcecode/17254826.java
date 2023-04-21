    private void drawItem(Graphics g, int x, int y, String text, boolean selected) {
        g.setColor(selected ? 0x808060 : 0x606060);
        g.fillRoundRect(x - 100, y - 14, 200, 24, 8, 8);
        if (selected) {
            g.setColor(0xffff00);
            g.drawRoundRect(x - 100, y - 14, 200, 24, 8, 8);
        }
        int h = g.getFont().getHeight();
        h = (h + 1) / 2;
        g.setColor(selected ? 0xffff00 : 0xc0c0c0);
        g.drawString(text, x - 96, y - h, Graphics.LEFT | Graphics.TOP);
    }
