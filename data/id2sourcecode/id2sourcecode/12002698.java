    public int viewToModel(Point pt) {
        if (pt.y > getHeight()) {
            return content.getLength() - 1;
        }
        FontMetrics metrics = getFontMetrics(getFont());
        int line = viewToLine(pt) - 1;
        try {
            char[] buffer = content.getBuffer();
            int lowOffset = content.getLineStartOffset(line);
            int highOffset = content.getLineEndOffset(line);
            int midOffset = (lowOffset + highOffset) / 2;
            int textWidth = 0;
            int offsetWidth = getGutterWidth(metrics);
            while (midOffset > lowOffset) {
                textWidth = metrics.charsWidth(buffer, lowOffset, midOffset - lowOffset);
                if (offsetWidth + textWidth < pt.x) {
                    lowOffset = midOffset;
                    offsetWidth += textWidth;
                } else {
                    highOffset = midOffset;
                }
                midOffset = (lowOffset + highOffset) / 2;
            }
            return lowOffset;
        } catch (BadLocationException ble) {
            return -1;
        }
    }
