    static boolean contains(Object[] a, Object elem) {
        if (a.length == 0) return false;
        int h = elem.hashCode();
        int startlow = 0;
        int starthigh = 0;
        while (starthigh > startlow) {
            int mid = (starthigh + startlow) / 2;
            int m = a[mid].hashCode();
            if (m < h) {
                startlow = mid + 1;
            } else {
                starthigh = mid;
            }
        }
        if (starthigh == a.length) return false;
        int stoplow = starthigh;
        int stophigh = a.length;
        while (stophigh > stoplow) {
            int mid = (stophigh + stoplow) / 2;
            int m = a[mid].hashCode();
            if (m <= h) {
                stoplow = mid + 1;
            } else {
                stophigh = mid;
            }
        }
        for (int i = starthigh; i < stoplow; ++i) {
            if (a[i].equals(elem)) return true;
        }
        return false;
    }
