    private final void sortIndices(T[] objects, final int from, final int to) {
        if (from >= to) {
            return;
        }
        final int pivotIndex = (from + to) / 2;
        int tmp;
        int middle = indices[pivotIndex];
        if (objects[indices[from]].compareTo(objects[middle]) > 0) {
            indices[pivotIndex] = indices[from];
            indices[from] = middle;
            middle = indices[pivotIndex];
        }
        if (objects[middle].compareTo(objects[indices[to]]) > 0) {
            indices[pivotIndex] = indices[to];
            indices[to] = middle;
            middle = indices[pivotIndex];
            if (objects[indices[from]].compareTo(objects[middle]) > 0) {
                indices[pivotIndex] = indices[from];
                indices[from] = middle;
                middle = indices[pivotIndex];
            }
        }
        int left = from + 1;
        int right = to - 1;
        if (left >= right) {
            return;
        }
        for (; ; ) {
            while (objects[indices[right]].compareTo(objects[middle]) > 0) {
                right--;
            }
            while ((left < right) && (objects[indices[left]].compareTo(objects[middle]) <= 0)) {
                left++;
            }
            if (left < right) {
                tmp = indices[left];
                indices[left] = indices[right];
                indices[right] = tmp;
                right--;
            } else {
                break;
            }
        }
        sortIndices(objects, from, left);
        sortIndices(objects, left + 1, to);
    }
