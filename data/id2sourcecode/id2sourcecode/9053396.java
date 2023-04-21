    public static int getMaxFittingFontSize(Graphics g, Font font, String string, int width, int height) {
        int minSize = 0;
        int maxSize = 288;
        int curSize = font.getSize();
        while (maxSize - minSize > 2) {
            FontMetrics fm = g.getFontMetrics(new Font(font.getName(), font.getStyle(), curSize));
            int fontWidth = fm.stringWidth(string);
            int fontHeight = fm.getLeading() + fm.getMaxAscent() + fm.getMaxDescent();
            if ((fontWidth > width) || (fontHeight > height)) {
                maxSize = curSize;
                curSize = (maxSize + minSize) / 2;
            } else {
                minSize = curSize;
                curSize = (minSize + maxSize) / 2;
            }
        }
        return curSize;
    }
