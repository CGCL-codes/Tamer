    @SuppressWarnings("unchecked")
    public V remove(Object key) {
        int k = checkKey(key);
        int index = Arrays.binarySearch(keyIndices, k);
        if (index >= 0) {
            V old = (V) (values[index]);
            Object[] newValues = Arrays.copyOf(values, values.length - 1);
            int[] newIndices = Arrays.copyOf(keyIndices, keyIndices.length - 1);
            for (int i = index; i < values.length - 1; ++i) {
                newValues[i] = values[i + 1];
                newIndices[i] = keyIndices[i + 1];
            }
            values = newValues;
            keyIndices = newIndices;
            return old;
        }
        return null;
    }
