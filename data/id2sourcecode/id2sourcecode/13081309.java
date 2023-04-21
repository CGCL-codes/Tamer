    public V remove(final long key) throws IllegalArgumentException, IllegalStateException {
        final int eIndex = findEntryIndex(key);
        if (eIndex < 0) return null;
        final long[] k = getKeys();
        final int numKeys = (null == k) ? 0 : k.length;
        final V[] o = getObjects();
        final int numObjects = (null == o) ? 0 : o.length;
        if (numKeys != numObjects) throw new IllegalStateException(getRemoveExceptionLocation(key) + " keys(" + numKeys + ")/objects(" + numObjects + ") arrays lengths mismatch");
        final V prev = o[eIndex];
        if ((null == prev) || (Long.MAX_VALUE == k[eIndex])) throw new IllegalStateException(getRemoveExceptionLocation(key) + " empty previous object at index=" + eIndex);
        for (int i = eIndex; i < (numKeys - 1); i++) {
            k[i] = k[i + 1];
            o[i] = o[i + 1];
        }
        k[numKeys - 1] = Long.MAX_VALUE;
        o[numObjects - 1] = null;
        updateSize(-1);
        return prev;
    }
