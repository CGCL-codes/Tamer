    public static TToken binsearch(Array<FV> arr, int start, int end) {
        int from = 0;
        int to = arr.length();
        while (from < to) {
            int it = (from + to) / 2;
            TToken at = (TToken) arr.getAt(it)._e();
            int off = TToken.offset(at);
            int len = TToken.length(at);
            if (off + len <= start) {
                from = it + 1;
                continue;
            }
            if (off > end) {
                to = it;
                continue;
            }
            if (off + len >= start && off + len > end) return at;
            return null;
        }
        return null;
    }
