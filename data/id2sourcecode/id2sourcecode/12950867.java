    public static byte[] revcompArray(final byte[] a, final int start, int stop, final byte compl, final byte[] b) {
        if (stop < 0 || stop > a.length) stop = a.length;
        byte tmp;
        final int len = stop - start;
        if (b != null) {
            assert (b.length >= len);
            for (int i = 0; i < len; i++) {
                b[i] = a[stop - 1 - i];
                if (b[i] >= 0 && b[i] < compl) b[i] = (byte) (compl - 1 - b[i]);
            }
            return b;
        } else {
            final int half = (start + stop) / 2;
            for (int i = start; i < half; i++) {
                tmp = a[i];
                a[i] = a[stop + start - 1 - i];
                a[stop + start - 1 - i] = tmp;
            }
            for (int i = start; i < stop; i++) {
                if (a[i] >= 0 && a[i] < compl) a[i] = (byte) (compl - 1 - a[i]);
            }
            return a;
        }
    }
