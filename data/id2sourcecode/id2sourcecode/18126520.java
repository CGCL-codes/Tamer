    public Font pickFont() {
        Font font = getFont();
        String fontName = font.getName();
        int style = font.getStyle();
        int size = (maxFontSize + minFontSize) / 2;
        return new Font(fontName, style, size);
    }
