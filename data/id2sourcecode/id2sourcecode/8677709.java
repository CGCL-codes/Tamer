    public int getValue(int index) {
        assert index >= Character.MIN_SUPPLEMENTARY_CODE_POINT && index <= Character.MAX_CODE_POINT : "Invalid code point:" + Integer.toHexString(index);
        int i = 0;
        int j = dataTable.length - 1;
        int k;
        for (; ; ) {
            k = (i + j) / 2;
            int start = dataTable[k] >> 8;
            int end = dataTable[k + 1] >> 8;
            if (index < start) {
                j = k;
            } else if (index > (end - 1)) {
                i = k;
            } else {
                return dataTable[k] & 0xFF;
            }
        }
    }
