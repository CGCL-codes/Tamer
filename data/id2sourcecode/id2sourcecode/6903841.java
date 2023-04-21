    private final int getIndexOf(long absoluteIndex) {
        int mid, lb, ub;
        int index = -1;
        boolean mide, pullupperbound, pulllowerbound;
        boolean cs1, cs2;
        int numberOfItr = 0;
        int length = length() * 2;
        lb = 0;
        ub = length() * 2;
        do {
            mid = (lb + ub) / 2;
            mide = mid % 2 == 0;
            cs1 = cs2 = pulllowerbound = pullupperbound = false;
            if (numberOfItr > length) {
                if (numberOfItr > 2 * length) {
                    throw new RuntimeException("Stuck in an infinite loop in getIndexOf(long ) due to malformed RangeArray");
                }
                System.err.println("Stuck in an infinite loop in getIndexOf(long ). getOff[mid=" + mid + "]=" + getOff(mid));
            }
            numberOfItr++;
            if (mide) {
                if (getOff(mid) <= absoluteIndex) cs1 = true;
                if (absoluteIndex <= getOff(mid + 1)) cs2 = true;
                if (cs1 && cs2) {
                    index = mid;
                } else if (!cs1 && cs2) {
                    pulllowerbound = false;
                    pullupperbound = true;
                } else if (cs1 && !cs2) {
                    pulllowerbound = true;
                    pullupperbound = false;
                }
            } else {
                if (getOff(mid) < absoluteIndex) cs1 = true;
                if (absoluteIndex < getOff(mid + 1)) cs2 = true;
                if (cs1 && cs2) {
                    index = mid;
                } else if (!cs1 && cs2) {
                    pulllowerbound = false;
                    pullupperbound = true;
                } else if (cs1 && !cs2) {
                    pulllowerbound = true;
                    pullupperbound = false;
                }
            }
            if (pullupperbound) ub = mid - 1;
            if (pulllowerbound) lb = mid + 1;
        } while (lb <= ub && index == -1);
        return index;
    }
