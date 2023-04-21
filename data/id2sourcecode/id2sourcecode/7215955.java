    public static void sort(Object[] array, int start, int end) {
        int middle = (start + end) / 2;
        if (start + 1 < middle) sort(array, start, middle);
        if (middle + 1 < end) sort(array, middle, end);
        if (start + 1 >= end) return;
        if (((Comparable) array[middle - 1]).compareTo(array[middle]) <= 0) return;
        if (start + 2 == end) {
            Object temp = array[start];
            array[start] = array[middle];
            array[middle] = temp;
            return;
        }
        int i1 = start, i2 = middle, i3 = 0;
        Object[] merge = new Object[end - start];
        while (i1 < middle && i2 < end) {
            merge[i3++] = ((Comparable) array[i1]).compareTo(array[i2]) <= 0 ? array[i1++] : array[i2++];
        }
        if (i1 < middle) System.arraycopy(array, i1, merge, i3, middle - i1);
        System.arraycopy(merge, 0, array, start, i2 - start);
    }
