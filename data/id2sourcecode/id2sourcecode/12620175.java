    private void paintBumps(Graphics g, JComponent c, int x, int y, int width, int height) {
        if (!useNarrowBumps()) {
            bumps.setBumpArea(width, height);
            bumps.paintIcon(c, g, x, y);
        } else {
            int maxWidth = UIManager.getInt(MAX_BUMPS_WIDTH_KEY);
            int myWidth = Math.min(maxWidth, width);
            int myHeight = Math.min(maxWidth, height);
            int myX = x + (width - myWidth) / 2;
            int myY = y + (height - myHeight) / 2;
            bumps.setBumpArea(myWidth, myHeight);
            bumps.paintIcon(c, g, myX, myY);
        }
    }
