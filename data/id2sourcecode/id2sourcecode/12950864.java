    public static void reverseArray(final byte[] array, final int from, final int to) {
        final int half = (to + from) / 2;
        int j = to - 1;
        int i = from;
        while (i < half) {
            final byte tmp = array[i];
            array[i] = array[j];
            array[j] = tmp;
            i++;
            j--;
        }
    }
