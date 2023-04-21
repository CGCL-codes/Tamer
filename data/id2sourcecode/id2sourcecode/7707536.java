    private static final void quickSort(Posting[] postings, int lo, int hi) {
        if (lo >= hi) return;
        int mid = (lo + hi) / 2;
        if (postings[lo].term.compareTo(postings[mid].term) > 0) {
            Posting tmp = postings[lo];
            postings[lo] = postings[mid];
            postings[mid] = tmp;
        }
        if (postings[mid].term.compareTo(postings[hi].term) > 0) {
            Posting tmp = postings[mid];
            postings[mid] = postings[hi];
            postings[hi] = tmp;
            if (postings[lo].term.compareTo(postings[mid].term) > 0) {
                Posting tmp2 = postings[lo];
                postings[lo] = postings[mid];
                postings[mid] = tmp2;
            }
        }
        int left = lo + 1;
        int right = hi - 1;
        if (left >= right) return;
        Term partition = postings[mid].term;
        for (; ; ) {
            while (postings[right].term.compareTo(partition) > 0) --right;
            while (left < right && postings[left].term.compareTo(partition) <= 0) ++left;
            if (left < right) {
                Posting tmp = postings[left];
                postings[left] = postings[right];
                postings[right] = tmp;
                --right;
            } else {
                break;
            }
        }
        quickSort(postings, lo, left);
        quickSort(postings, left + 1, hi);
    }
