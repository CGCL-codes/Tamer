    protected void drawBox(Graphics graphics) throws GUIException {
        int h;
        if (getHeight() % 2 == 0) {
            h = getHeight() - 2;
        } else {
            h = getHeight() - 1;
        }
        int alpha = getBaseColor().a;
        Color faceColor = getBaseColor();
        faceColor.a = alpha;
        Color highlightColor = faceColor.add(0x303030);
        highlightColor.a = alpha;
        Color shadowColor = faceColor.subtract(0x303030);
        shadowColor.a = alpha;
        graphics.setColor(getBackgroundColor());
        int hh = (h + 1) / 2;
        for (int i = 1; i <= hh; ++i) {
            graphics.drawLine(hh - i + 1, i, hh + i - 1, i);
        }
        for (int i = 1; i < hh; ++i) {
            graphics.drawLine(hh - i + 1, h - i, hh + i - 1, h - i);
        }
        graphics.setColor(shadowColor);
        graphics.drawLine(hh, 0, 0, hh);
        graphics.drawLine(hh + 1, 1, h - 1, hh - 1);
        graphics.setColor(highlightColor);
        graphics.drawLine(1, hh + 1, hh, h);
        graphics.drawLine(hh + 1, h - 1, h, hh);
        graphics.setColor(getForegroundColor());
        int hhh = hh - 3;
        if (isMarked()) {
            for (int i = 0; i < hhh; ++i) {
                graphics.drawLine(hh - i, 4 + i, hh + i, 4 + i);
            }
            for (int i = 0; i < hhh; ++i) {
                graphics.drawLine(hh - i, h - 4 - i, hh + i, h - 4 - i);
            }
        }
    }
