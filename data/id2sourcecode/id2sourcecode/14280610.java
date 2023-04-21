    void replaceStyleRanges(int start, int length, StyleRange[] ranges) {
        clearStyle(new StyleRange(start, length, null, null));
        int high = styleCount;
        int low = -1;
        int index = high;
        while (high - low > 1) {
            index = (high + low) / 2;
            StyleRange style = styles[index];
            if (start <= style.start) {
                high = index;
            } else {
                low = index;
            }
        }
        insertStyles(ranges, high);
    }
