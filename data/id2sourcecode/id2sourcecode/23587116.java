    int medianOf3(final double[] work, final int begin, final int end) {
        final int inclusiveEnd = end - 1;
        final int middle = begin + (inclusiveEnd - begin) / 2;
        final double wBegin = work[begin];
        final double wMiddle = work[middle];
        final double wEnd = work[inclusiveEnd];
        if (wBegin < wMiddle) {
            if (wMiddle < wEnd) {
                return middle;
            } else {
                return (wBegin < wEnd) ? inclusiveEnd : begin;
            }
        } else {
            if (wBegin < wEnd) {
                return begin;
            } else {
                return (wMiddle < wEnd) ? inclusiveEnd : middle;
            }
        }
    }
