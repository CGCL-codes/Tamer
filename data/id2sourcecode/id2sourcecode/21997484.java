    private static CharSeqCounter counter(CharSeqCounter[] counters, int start, int end) {
        if (end <= start) {
            String msg = "Too few counters provided.";
            throw new IllegalArgumentException(msg);
        }
        if (end - start == 1) return counters[start];
        int mid = start + (end - start) / 2;
        return new CharSeqMultiCounter(counter(counters, start, mid), counter(counters, mid, end));
    }
