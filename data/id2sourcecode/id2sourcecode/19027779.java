    public int findDayIndex(int id) {
        Day d;
        int i, high, low;
        for (low = -1, high = numDays(); high - low > 1; ) {
            i = (high + low) / 2;
            d = dayByIndex(i);
            if (id <= d.getId()) high = i; else low = i;
        }
        d = dayByIndex(high);
        if (d == null) return -1; else if (id != d.getId()) return -1;
        return high;
    }
