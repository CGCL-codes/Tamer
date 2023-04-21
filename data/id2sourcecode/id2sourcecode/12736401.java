    public synchronized void set(int virtualIndex, double value) {
        if (value != sparseValue) {
            if (indexUsedMap.get(virtualIndex) == true) {
                int realIndex = findIndex(virtualIndex);
                values[realIndex] = value;
            } else {
                if (realSize == indices.length) {
                    growArray();
                }
                if (realSize == 0 || virtualIndex > indices[realSize - 1]) {
                    indices[realSize] = virtualIndex;
                    values[realSize] = value;
                } else {
                    for (int i = realSize - 1; i >= 0; i--) {
                        if (indices[i] > virtualIndex) {
                            indices[i + 1] = indices[i];
                            values[i + 1] = values[i];
                            if (i == 0) {
                                indices[i] = virtualIndex;
                                values[i] = value;
                                break;
                            }
                        } else if (indices[i] < virtualIndex) {
                            indices[i + 1] = virtualIndex;
                            values[i + 1] = value;
                            break;
                        } else {
                            System.out.println("ERROR 6: Index already contained?");
                        }
                    }
                }
                indexUsedMap.set(virtualIndex, true);
                realSize++;
            }
        } else if (indexUsedMap.get(virtualIndex) == true) {
            int realIndex = findIndex(virtualIndex);
            int maxIndex = realSize - 1;
            for (int i = realIndex; i < maxIndex; i++) {
                indices[i] = indices[i + 1];
                values[i] = values[i + 1];
            }
            indices[maxIndex] = -1;
            values[maxIndex] = 0.0;
            indexUsedMap.set(virtualIndex, false);
            realSize--;
        }
    }
