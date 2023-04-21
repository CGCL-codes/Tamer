    void setComponentGapHeight(int value) {
        componentGapHeight = value;
        final int fontHeight = getFontHeight();
        if (componentGapHeight / 2 + fontHeight / 2 < Chart.defaultComponentArcSize) {
            componentArcSize = (componentGapHeight + fontHeight) / 2;
        } else {
            componentArcSize = Chart.defaultComponentArcSize;
        }
        if (componentArcSize % 2 != 0) {
            componentArcSize -= 1;
        }
    }
