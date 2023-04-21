    protected int[] getNumericInputAttributeIndices() {
        int[] indices = store.getInputAttributesIndices();
        for (int i = 0; i < indices.length; i++) {
            if (store.getAttributeType(i) != PreprocessingStorage.DataType.NUMERIC) {
                logger.warn("Implemented outlier detection methods can not work on non-numeric game.data :(. Skipping attribute " + store.getAttributeName(i));
                int[] newIndices = new int[indices.length - 1];
                for (int j = 0; j < newIndices.length; j++) {
                    if (j < i) newIndices[j] = indices[j]; else newIndices[j] = indices[j + 1];
                }
                indices = newIndices;
                i--;
            }
        }
        return indices;
    }
