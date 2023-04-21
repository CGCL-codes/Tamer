    private int getDBUserNameBisection(HttpMessage msg, String param, String value, int charPos, int rangeLow, int rangeHigh) throws HttpException, IOException {
        if (rangeLow == rangeHigh) {
            return rangeLow;
        }
        int medium = (rangeLow + rangeHigh) / 2;
        boolean result = getDBUserNameQuery(msg, param, value, charPos, medium);
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
        int charResult = getDBUserNameBisection(msg, param, value, charPos, rangeLow, rangeHigh);
        return charResult;
    }
