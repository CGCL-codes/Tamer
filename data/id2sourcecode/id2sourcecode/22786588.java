    public final int getId(int bound) {
        int lower;
        int upper;
        if (cache == null) {
            lower = 0;
            upper = size() - 1;
        } else {
            final int key = bound >> CACHE_SHIFT;
            lower = (int) cache.get(key);
            upper = (int) cache.get(key + 1);
        }
        while (lower != upper) {
            int id = lower + (upper - lower) / 2;
            int loc = getUpperBound(id);
            if (loc > bound) upper = id; else if (loc < bound) lower = id + 1; else return id + 1;
        }
        return lower;
    }
