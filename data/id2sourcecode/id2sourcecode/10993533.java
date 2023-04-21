    private final int _getIndex(char c) {
        int high = charset.length, low = -1, probe;
        while (high - low > 1) {
            probe = (high + low) / 2;
            if (charset[probe] < c) low = probe; else high = probe;
        }
        if (!(high >= charset.length || charset[high] != c)) return high;
        return _getIndex('_');
    }
