    public int getLineAtOffset(int charPosition) {
        int position;
        if ((charPosition > getCharCount()) || (charPosition < 0)) error(SWT.ERROR_INVALID_ARGUMENT);
        if (charPosition < gapStart) {
            position = charPosition;
        } else {
            position = charPosition + (gapEnd - gapStart);
        }
        if (lineCount > 0) {
            int lastLine = lineCount - 1;
            if (position == lines[lastLine][0] + lines[lastLine][1]) return lastLine;
        }
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
