    public static final int[] sortIndices(final double[] objects, final int[] indices, final int from, final int to) {
        if (from >= to) {
            return indices;
        }
        final int pivotIndex = (from + to) / 2;
        int tmp;
        int middle = indices[pivotIndex];
        if (objects[indices[from]] > objects[middle]) {
            indices[pivotIndex] = indices[from];
            indices[from] = middle;
            middle = indices[pivotIndex];
        }
        if (objects[middle] > objects[indices[to]]) {
            indices[pivotIndex] = indices[to];
            indices[to] = middle;
            middle = indices[pivotIndex];
            if (objects[indices[from]] > objects[middle]) {
                indices[pivotIndex] = indices[from];
                indices[from] = middle;
                middle = indices[pivotIndex];
            }
        }
        int left = from + 1;
        int right = to - 1;
        if (left >= right) {
            return indices;
        }
        for (; ; ) {
            while (objects[indices[right]] > objects[middle]) {
                right--;
            }
            while ((left < right) && (objects[indices[left]] <= objects[middle])) {
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
        sortIndices(objects, indices, from, left);
        sortIndices(objects, indices, left + 1, to);
        return indices;
    }
