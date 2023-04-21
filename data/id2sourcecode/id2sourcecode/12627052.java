    public int lookupToken(int base, String key) {
        int start = SPECIAL_CASES_INDEXES[base];
        int end = SPECIAL_CASES_INDEXES[base + 1] - 1;
        while (start <= end) {
            int half = (start + end) / 2;
            int comp = SPECIAL_CASES_KEYS[half].compareTo(key);
            if (comp == 0) return SPECIAL_CASES_VALUES[half]; else if (comp < 0) start = half + 1; else end = half - 1;
        }
        return base;
    }
