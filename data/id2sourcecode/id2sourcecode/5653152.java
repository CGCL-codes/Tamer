    public static byte getCharCharClass(char ch) {
        if (ch < QUICK_LUT_SIZE) {
            if (quickLut == null) buildQuickLut();
            return quickLut[ch];
        }
        int len = raw_data.length;
        int l = 0;
        int r = (len / 2) - 1;
        int entry = (l + r) / 2;
        while (l <= r) {
            char min = raw_data[2 * entry];
            char max = raw_data[2 * entry + 1];
            if (ch < min) r = entry - 1; else if (ch > max) l = entry + 1; else break;
            entry = (l + r) / 2;
        }
        return raw_classes[entry];
    }
