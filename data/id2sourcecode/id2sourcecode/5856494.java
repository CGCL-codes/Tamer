    public InternalNodeArrayMap split() {
        InternalNodeArrayMap newMap = new InternalNodeArrayMap(keys.length);
        final int mid = currentSize / 2;
        int count = 0;
        newMap.nodes[0] = nodes[mid + 1];
        for (int i = mid + 1; i < currentSize; i++) {
            newMap.keys[count] = keys[i];
            newMap.nodes[++count] = nodes[i + 1];
        }
        for (int i = mid; i < currentSize; i++) {
            nodes[i + 1] = null;
        }
        newMap.currentSize = currentSize - mid - 1;
        currentSize = mid;
        return newMap;
    }
