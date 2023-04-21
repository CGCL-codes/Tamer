    public static double[] rank(double data[], boolean reversed) {
        int size = data.length;
        double dup[] = new double[data.length];
        System.arraycopy(data, 0, dup, 0, data.length);
        Arrays.sort(dup);
        double ranks[] = new double[data.length];
        for (int i = 0; i < size; ++i) {
            double val = data[i];
            int low = 0;
            int mid = size / 2;
            int hgh = size - 1;
            while (val != dup[mid]) {
                if (val < dup[mid]) hgh = mid - 1; else low = mid + 1;
                mid = (low + hgh) / 2;
            }
            int jt = 0;
            int ties = 0;
            jt = mid - 1;
            while (jt >= 0 && dup[jt--] == val) ties++;
            jt = mid + 1;
            if (ties != 0) mid -= ties;
            while (jt < size && dup[jt++] == val) ties++;
            if (ties == 0) ranks[i] = (reversed ? size - mid : mid + 1); else ranks[i] = (reversed ? size - (0.5 * (mid + mid + ties) + 1) : (0.5 * (mid + mid + ties) + 1));
        }
        return ranks;
    }
