    private static int[] addIndex(final int[] indexes, final int newIndex, final boolean excludeFirstIndex) {
        int[] newIndices = null;
        if (excludeFirstIndex) {
            newIndices = new int[indexes.length];
            for (int i = 0, z = indexes.length - 1; i < z; i++) {
                newIndices[i] = indexes[i + 1];
            }
        } else {
            newIndices = new int[indexes.length + 1];
            for (int i = 0, z = indexes.length; i < z; i++) {
                newIndices[i] = indexes[i];
            }
        }
        newIndices[newIndices.length - 1] = newIndex;
        return newIndices;
    }
