    protected int selectPivot(int[] a, int fromIndex, int toIndex) {
        final int first = fromIndex, last = toIndex - 1, mid = (fromIndex + toIndex) / 2;
        final int size = toIndex - fromIndex;
        switch(PIVOT_ALG) {
            case 1:
                return mid;
            case 2:
                return Math.abs(rand.nextInt()) % size + first;
            case 3:
                return median(a, first, mid, last);
            case 4:
                return ninther(a, first, mid, last);
            case 5:
                if (size > 100) {
                    if (size > 1000) return ninther(a, first, mid, last); else return median(a, first, mid, last);
                } else return mid;
            default:
                return 0;
        }
    }
