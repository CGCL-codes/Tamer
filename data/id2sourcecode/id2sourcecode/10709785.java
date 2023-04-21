    private int getTableNameBisection(HttpMessage msg, String param, String value, int charPos, int rangeLow, int rangeHigh, int row) throws HttpException, IOException {
        if (rangeLow == rangeHigh) {
            return rangeLow;
        }
        int medium = (rangeLow + rangeHigh) / 2;
        boolean result = getTableNameQuery(msg, param, value, charPos, medium, row);
        if (rangeHigh - rangeLow < 2) {
            if (result) {
                return rangeHigh;
            } else {
                return rangeLow;
            }
        }
        if (result) {
            rangeLow = medium;
        } else {
            rangeHigh = medium;
        }
        int charResult = getTableNameBisection(msg, param, value, charPos, rangeLow, rangeHigh, row);
        return charResult;
    }
