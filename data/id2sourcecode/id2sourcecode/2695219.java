    int getLineAtPhysicalOffset(int position) {
        int high = lineCount;
        int low = -1;
        int index = lineCount;
        while (high - low > 1) {
            index = (high + low) / 2;
            int lineStart = lines[index][0];
            int lineEnd = lineStart + lines[index][1] - 1;
            if (position <= lineStart) {
                high = index;
            } else if (position <= lineEnd) {
                high = index;
                break;
            } else {
                low = index;
            }
        }
        return high;
    }
