    protected void handleComputeFields(int julianDay) {
        super.handleComputeFields(julianDay);
        int year = internalGet(EXTENDED_YEAR);
        int low = 0;
        if (year > ERAS[ERAS.length - 3]) {
            low = CURRENT_ERA;
        } else {
            int high = ERAS.length / 3;
            while (low < high - 1) {
                int i = (low + high) / 2;
                int diff = year - ERAS[i * 3];
                if (diff == 0) {
                    diff = internalGet(MONTH) - (ERAS[i * 3 + 1] - 1);
                    if (diff == 0) {
                        diff = internalGet(DAY_OF_MONTH) - ERAS[i * 3 + 2];
                    }
                }
                if (diff >= 0) {
                    low = i;
                } else {
                    high = i;
                }
            }
        }
        internalSet(ERA, low);
        internalSet(YEAR, year - ERAS[low * 3] + 1);
    }
