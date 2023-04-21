    private static int partition(Comparable[] a, int first, int last) {
        int mid = (first + last) / 2;
        sortFirstMiddleLast(a, first, mid, last);
        swap(a, mid, last - 1);
        int pivotIndex = last - 1;
        Comparable pivot = a[pivotIndex];
        int indexFromLeft = first + 1;
        int indexFromRight = last - 2;
        boolean done = false;
        while (!done) {
            while (a[indexFromLeft].compareTo(pivot) < 0) indexFromLeft++;
            while (a[indexFromRight].compareTo(pivot) > 0) indexFromRight--;
            if (indexFromLeft < indexFromRight) {
                swap(a, indexFromLeft, indexFromRight);
                indexFromLeft++;
                indexFromRight++;
            } else done = true;
        }
        swap(a, pivotIndex, indexFromLeft);
        pivotIndex = indexFromLeft;
        return pivotIndex;
    }
