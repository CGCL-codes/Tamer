    private void calcOffsets(int bottom, int top) {
        if (bottom == top || bottom + 1 == top) return;
        int index = (bottom + top) / 2;
        long pos = positions[index];
        long est = (positions[bottom] + positions[top]) / 2;
        calcOffsets(bottom, index);
        calcOffsets(index, top);
        positions[index] = pos - est;
    }
