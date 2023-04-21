    private Object[] sort(Object[] obj) {
        if (size == 0) {
            return null;
        }
        Object[] dest = new Object[size];
        System.arraycopy(obj, 0, dest, 0, size);
        sort(obj, dest, 0, size, 0);
        int i = 0, j = 0;
        for (i = 0; i < size - 1; i++) {
            if (dest[i] != dest[i + 1]) {
                dest[i] = dest[i + 1];
                j++;
            }
        }
        return dest;
    }
