    int searchForStyle(int start, int end) {
        int high = styleCount;
        int low = -1;
        int index = high;
        while (high - low > 1) {
            index = (high + low) / 2;
            StyleRange style = styles[index];
            int styleEnd = style.start + style.length - 1;
            if (start <= style.start || end <= styleEnd || (start > style.start && styleEnd >= start && styleEnd < end)) {
                high = index;
            } else {
                low = index;
            }
        }
        return high;
    }
