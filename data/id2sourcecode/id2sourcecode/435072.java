    public long countTriplets(int minx, int maxx, int miny, int maxy, int minz, int maxz) {
        if (minx > maxx || miny > maxy || (long) minx * miny > maxz || (long) maxx * maxy < minz) return 0;
        if ((long) minx * miny >= minz && (long) maxx * maxy <= maxz) return (long) (maxx - minx + 1) * (maxy - miny + 1);
        if ((maxx - minx < 10000) && (maxy - miny < 10000)) return countTriplets2(minx, maxx, miny, maxy, minz, maxz);
        long count = 0;
        int midx = (minx + maxx) / 2;
        int midy = (miny + maxy) / 2;
        count += countTriplets(minx, midx, miny, midy, minz, maxz) + countTriplets(midx + 1, maxx, miny, midy, minz, maxz) + countTriplets(minx, midx, midy + 1, maxy, minz, maxz) + countTriplets(midx + 1, maxx, midy + 1, maxy, minz, maxz);
        return count;
    }
