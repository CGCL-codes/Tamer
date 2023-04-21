    protected void removeDuplicates(int numDuplicates) {
        if (indices == null) return;
        if (numDuplicates == 0) for (int i = 1; i < size; i++) if (indices[i - 1] == indices[i]) numDuplicates++;
        if (numDuplicates == 0) return;
        assert (indices.length - numDuplicates > 0) : "size=" + size + " indices.length=" + indices.length + " numDuplicates=" + numDuplicates;
        int[] newIndices = new int[size - numDuplicates];
        double[] newValues = values == null ? null : new double[size - numDuplicates];
        newIndices[0] = indices[0];
        assert (indices.length >= size);
        for (int i = 0, j = 0; i < size - 1; i++) {
            if (indices[i] == indices[i + 1]) {
                if (values != null) newValues[j] += values[i];
            } else {
                newIndices[j] = indices[i];
                if (values != null) newValues[j] += values[i];
                j++;
            }
            if (i == size - 2) {
                if (values != null) newValues[j] += values[i + 1];
                newIndices[j] = indices[i + 1];
            }
        }
        this.indices = newIndices;
        this.values = newValues;
        this.size -= numDuplicates;
        this.maxSortedIndex = size - 1;
    }
