    protected static Font calculateBoundedFont(String testLabel, String fontName, int fontStyle, Rectangle2D boundingRect, FontRenderContext renderContext) {
        Font currentFont = null;
        int currentFontSize = 1;
        int tooSmall = currentFontSize;
        int tooBig = Integer.MAX_VALUE;
        while (tooBig == Integer.MAX_VALUE) {
            final Font font = new Font(fontName, fontStyle, currentFontSize);
            if (fitsIntoBound(boundingRect, renderContext, testLabel, font)) {
                tooSmall = currentFontSize;
                currentFontSize *= 2;
            } else {
                tooBig = currentFontSize;
            }
        }
        boolean searching = true;
        while (searching) {
            currentFontSize = (tooSmall + tooBig) / 2;
            final Font font = new Font(fontName, fontStyle, currentFontSize);
            if (fitsIntoBound(boundingRect, renderContext, testLabel, font)) {
                currentFont = font;
                if (tooSmall == currentFontSize) {
                    searching = false;
                } else {
                    tooSmall = currentFontSize;
                }
            } else {
                tooBig = currentFontSize;
            }
        }
        ;
        return currentFont;
    }
