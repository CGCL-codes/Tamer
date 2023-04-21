    private Point calculateWidgetAlignValuesV(VERTICAL_ALIGN align, int cellFrom, int cellLength, int widgetLength) {
        int from = 0;
        int length = 0;
        if (align == VERTICAL_ALIGN.FILL) {
            from = cellFrom;
            length = cellLength;
        } else {
            if (align == VERTICAL_ALIGN.TOP) {
                length = Math.min(widgetLength, cellLength);
                from = cellFrom;
            }
            if (align == VERTICAL_ALIGN.BOTTOM) {
                length = Math.min(widgetLength, cellLength);
                from = cellFrom + cellLength - length;
            }
            if (align == VERTICAL_ALIGN.CENTER) {
                length = Math.min(widgetLength, cellLength);
                from = cellFrom + (cellLength - length) / 2;
            }
        }
        return new Point(from, length);
    }
