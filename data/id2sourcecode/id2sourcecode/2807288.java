    private void drawChar(Graphics g, int x, int y, WoWchar chr, int index) {
        boolean selected = (index == m_selected);
        g.setColor(selected ? 0x808060 : 0x606060);
        g.fillRoundRect(x - 100, y - 26, 200, 52, 8, 8);
        if (selected) {
            g.setColor(0xffff00);
            g.drawRoundRect(x - 100, y - 26, 200, 52, 8, 8);
        }
        if (chr.icon() != null) g.drawImage(chr.icon(), x + 96, y, Graphics.RIGHT | Graphics.VCENTER);
        String name = (index + 1) + " " + chr.name();
        if ((chr.m_flags & WoWchar.CHAR_GHOST) != 0) name += " (Ghost)";
        int h = g.getFont().getHeight();
        h = (h + 1) / 2;
        g.setColor(selected ? 0xffff00 : 0xc0c0c0);
        g.drawString(name, x - 96, y - 25, Graphics.LEFT | Graphics.TOP);
        g.setColor(selected ? 0x0000ff : 0x000000);
        g.drawString(chr.level(), x - 96, y - h, Graphics.LEFT | Graphics.TOP);
        g.drawString(chr.area(), x - 96, y + 25, Graphics.LEFT | Graphics.BOTTOM);
    }
