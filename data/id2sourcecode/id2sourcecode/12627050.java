    private int nextState(char c, int state) {
        int start = SCANNER_TABLE_INDEXES[state];
        int end = SCANNER_TABLE_INDEXES[state + 1] - 1;
        while (start <= end) {
            int half = (start + end) / 2;
            if (SCANNER_TABLE[half][0] == c) return SCANNER_TABLE[half][1]; else if (SCANNER_TABLE[half][0] < c) start = half + 1; else end = half - 1;
        }
        return -1;
    }
