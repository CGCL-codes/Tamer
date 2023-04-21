    public Color getChannelColor() {
        if (lut == null || mode == GRAYSCALE) return Color.black;
        IndexColorModel cm = lut[getChannelIndex()];
        if (cm == null) return Color.black;
        int index = cm.getMapSize() - 1;
        int r = cm.getRed(index);
        int g = cm.getGreen(index);
        int b = cm.getBlue(index);
        if (r < 100 || g < 100 || b < 100) return new Color(r, g, b); else return Color.black;
    }
