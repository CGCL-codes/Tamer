    protected void leftShiftFrom(int position, boolean shiftChildren) {
        for (int i = position; i < nbKeys - 1; i++) {
            keys[i] = keys[i + 1];
            values[i] = values[i + 1];
            if (shiftChildren) {
                moveChildFromTo(i + 1, i, false);
            }
        }
        keys[nbKeys - 1] = null;
        values[nbKeys - 1] = null;
        if (shiftChildren) {
            moveChildFromTo(nbKeys, nbKeys - 1, false);
            setNullChildAt(nbKeys);
        }
    }
