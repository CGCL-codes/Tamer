    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        int count = getValuesCount();
        int currentIndex = index - count;
        if (currentIndex < 0) currentIndex = count + currentIndex;
        int newIndex = currentIndex + 1;
        boolean isFirst = true;
        final int pad = 3;
        int x = 0;
        int step = info.getStepWidth();
        int topLine = pad;
        int bottomLine = getHeight() - pad;
        int centerLine = topLine + (bottomLine - topLine) / 2;
        int span = 0;
        for (int i = 0; i < count; i++, currentIndex = newIndex, newIndex++) {
            if (info.getSelectedColumn() == i) {
                g.setColor(COLOR_SELECTED);
                g.fillRect(step * i, 0, step, getHeight());
            }
            span++;
            if (newIndex >= values.length) newIndex = 0;
            if (((i + 1) < count) && (values[currentIndex] == values[newIndex])) continue;
            int w = span * step;
            int startX = x + pad;
            int endX = x + w - pad;
            span = 0;
            g.setColor(COLOR_LINE);
            g.drawLine(startX, topLine, endX, topLine);
            g.drawLine(startX, bottomLine, endX, bottomLine);
            if (!isFirst) {
                g.drawLine(startX, topLine, x, centerLine);
                g.drawLine(startX, bottomLine, x, centerLine);
            }
            g.drawLine(endX, topLine, endX + pad, centerLine);
            g.drawLine(endX, bottomLine, endX + pad, centerLine);
            Rectangle clip = g.getClipBounds();
            g.setFont(info.getFont());
            g.setColor(COLOR_TEXT);
            Rectangle textFrame = new Rectangle(x, clip.y, w - 1, clip.height);
            g.setClip(clip.intersection(textFrame));
            String stateName = names[values[currentIndex]];
            g.drawString(stateName, startX, bottomLine - pad);
            g.setClip(clip);
            isFirst = false;
            x += w;
        }
    }
