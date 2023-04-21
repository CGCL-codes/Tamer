    private Point calculateWidgetAlignValuesH(HORIZONTAL_ALIGN horizontal_align, int cellFrom, int cellLength, int widgetLength) {
        int from = 0;
        int length = 0;
        if (horizontal_align == HORIZONTAL_ALIGN.FILL) {
            from = cellFrom;
            length = cellLength;
        } else {
            if (horizontal_align == HORIZONTAL_ALIGN.LEFT) {
                length = Math.min(widgetLength, cellLength);
                from = cellFrom;
            }
            if (horizontal_align == HORIZONTAL_ALIGN.RIGHT) {
                length = Math.min(widgetLength, cellLength);
                from = cellFrom + cellLength - length;
            }
            if (horizontal_align == HORIZONTAL_ALIGN.CENTER) {
                length = Math.min(widgetLength, cellLength);
                from = cellFrom + (cellLength - length) / 2;
            }
        }
        return new Point(from, length);
    }
