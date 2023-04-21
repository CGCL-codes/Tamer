    protected void calcLabelPosition() {
        int w = getWidth();
        int h = getHeight();
        FontMetrics fm = getFontMetrics();
        int sh = fm.getHeight();
        valuex = w - insets.right - 4 - valuew;
        valuey = (h - valueh) / 2;
        namex = insets.left;
        namey = (h + sh) / 2;
    }
