    public synchronized int getColumn(Line line, int x) {
        if (line instanceof ImageLine) return 0;
        int maxCols = getMaxCols();
        formatLine(line, shift, maxCols);
        Graphics2D g2d = (Graphics2D) getGraphics();
        int begin = 0;
        int end = maxCols;
        while (end - begin > 4) {
            int pivot = (begin + end) / 2;
            int width = measureLine(g2d, textArray, pivot, formatArray);
            if (width + gutterWidth > x) end = pivot + 1; else begin = pivot - 1;
        }
        for (int i = begin; i < end; i++) {
            int width = measureLine(g2d, textArray, i, formatArray);
            if (width + gutterWidth > x) return i - 1;
        }
        return 0;
    }
