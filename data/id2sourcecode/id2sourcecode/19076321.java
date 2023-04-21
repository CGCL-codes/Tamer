    private String prepareErrorBars(int x, int y1, int y2, Cell c) {
        StringBuffer sb = new StringBuffer();
        int low = 3;
        int high = 7;
        int halfTab = 2;
        int y = (y1 + y2) / 2;
        y = TOP - y;
        sb.append(mockDrawLine(x - low, y, x + high, y));
        sb.append(mockDrawLine(x - low, y - halfTab, x - low, y + halfTab));
        sb.append(mockDrawLine(x + high, y - halfTab, x + high, y + halfTab));
        sb.append(COMMENT + c.getName());
        return sb.toString();
    }
