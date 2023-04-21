    public int getLineAtOffset(int offset) {
        int lastLine = visualLineCount - 1;
        int lastChar;
        if (visualLineCount == 0) {
            return logicalContent.getLineAtOffset(offset);
        }
        lastChar = visualLines[lastLine][LINE_OFFSET] + visualLines[lastLine][LINE_LENGTH];
        if (offset < 0 || (offset > 0 && offset > lastChar)) {
            SWT.error(SWT.ERROR_INVALID_ARGUMENT);
        }
        if (offset == lastChar) {
            return lastLine;
        }
        int high = visualLineCount;
        int low = -1;
        int index = visualLineCount;
        while (high - low > 1) {
            index = (high + low) / 2;
            int lineStart = visualLines[index][LINE_OFFSET];
            if (offset >= lineStart) {
                int lineEnd = lineStart + visualLines[index][LINE_LENGTH];
                low = index;
                if (offset <= lineEnd) {
                    break;
                }
            } else {
                high = index;
            }
        }
        if (low > 0 && offset == visualLines[low - 1][LINE_OFFSET] + visualLines[low - 1][LINE_LENGTH]) {
            low--;
        }
        return low;
    }
