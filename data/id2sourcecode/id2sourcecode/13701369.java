    private long search(long[] matches, long value) {
        int left = 0;
        int right = matches.length - 1;
        int middle = right / 2;
        long inserSize;
        while (left <= right) {
            inserSize = matches[middle] + readLength - value;
            if (inserSize >= minDistance && inserSize <= maxDistance) {
                insertSizes[(int) inserSize].incrementAndGet();
                return matches[middle] + readLength - 1;
            }
            if (inserSize < minDistance) {
                left = middle + 1;
            } else {
                right = middle - 1;
            }
            middle = (left + right) / 2;
        }
        return Constants.INVALID;
    }
