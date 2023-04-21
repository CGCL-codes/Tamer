    public void draw(Graphics g) {
        final int S2 = (SIZE + 1) / 2;
        final int x0 = x - S2;
        final int y0 = y - S2;
        if (scoreId != scoreChangeId) {
            if (maxScore == minScore) {
                colorBg = COLOR_BG;
                color = COLOR;
            } else {
                int c = (int) ((long) (score - minScore) * 255 / (maxScore - minScore));
                if (c < 0) {
                    c = 0;
                } else if (c > 255) {
                    c = 255;
                }
                colorBg = new Color(c, c, c);
                color = c > 127 ? COLOR : COLOR_2;
            }
            scoreId = scoreChangeId;
        }
        g.setColor(colorBg);
        g.fillOval(x0, y0, SIZE, SIZE);
        g.setColor(color);
        g.drawOval(x0, y0, SIZE, SIZE);
        final int off = SIZE % 2;
        if (isInit()) {
            g.drawString(name.substring(0, 1), x + DELTA_X - off, y + DELTA_Y - off);
        }
    }
