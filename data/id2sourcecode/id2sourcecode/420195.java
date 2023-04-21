    public boolean delete(long value) {
        int j = find(value);
        if (j == nElems) return false; else {
            for (int k = j; k < nElems; k++) a[k] = a[k + 1];
            nElems--;
            return true;
        }
    }
