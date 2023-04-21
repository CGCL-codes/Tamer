    public boolean isSelectedIndex(int index) {
        if (index < overallRange.min || index > overallRange.max) return false;
        int first = 0;
        int last = selectionRanges.size() - 1;
        while (last >= first) {
            int m = (first + last) / 2;
            Range range = selectionRanges.get(m);
            if (index >= range.min && index <= range.max) {
                return true;
            }
            if (index > range.max) {
                first = m + 1;
            } else {
                last = m - 1;
            }
        }
        return false;
    }
