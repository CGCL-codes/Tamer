    private void drawClockInfo(Graphics g) {
        Clock clock = Clock.getInstance();
        int winWidth = getWidth();
        int winHeight = getHeight();
        g.setColor(Color.BLACK);
        String timeStr = clock.getTimeString();
        Font timeFont = getTimeFont(g);
        g.setFont(timeFont);
        FontMetrics timeFontMetric = g.getFontMetrics();
        Rectangle2D timeStrBounds = timeFontMetric.getStringBounds(timeStr, g);
        int timeStrWidth = (int) timeStrBounds.getWidth();
        int timeStrHeight = (int) timeStrBounds.getHeight();
        int timeStrX = (winWidth - timeStrWidth) / 2;
        int timeStrY = (winHeight + timeStrHeight) / 2;
        int timeStrOffset = timeStrHeight / 8 / 2;
        g.drawString(timeStr, timeStrX, timeStrY);
        String dateStr = clock.getDateString();
        Font dateFont = getDateFont(g);
        g.setFont(dateFont);
        FontMetrics dateFontMetric = g.getFontMetrics();
        Rectangle2D dateStrBounds = dateFontMetric.getStringBounds(dateStr, g);
        g.drawString(dateStr, (winWidth - (int) dateStrBounds.getWidth()) / 2, timeStrY - timeStrHeight - timeStrOffset);
        Font secFont = getSecondFont(g);
        g.setFont(secFont);
        int sec = clock.getSecond();
        int secBarBlockSize = timeStrWidth / 60;
        int secBarBlockY = timeStrY + timeStrOffset;
        for (int n = 0; n < sec; n++) {
            int x = timeStrX + (secBarBlockSize * n);
            g.fillRect(x, secBarBlockY, secBarBlockSize - 1, DEFAULT_SECOND_BLOCK_HEIGHT);
        }
        if (sec != 0 && (sec % 10) == 0) {
            int x = timeStrX + (secBarBlockSize * sec);
            g.drawString(Integer.toString(sec), x + secBarBlockSize, secBarBlockY + DEFAULT_SECOND_BLOCK_HEIGHT);
        }
    }
