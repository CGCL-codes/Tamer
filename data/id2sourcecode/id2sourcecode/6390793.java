    private static void qsort_h(int[] array, int oi, int oj, SortComparatorL comp) {
        int[] stack = new int[MAXSTACKSIZE];
        int top = -1;
        int pivot;
        int pivotindex, l, r;
        int tmp;
        stack[++top] = oi;
        stack[++top] = oj;
        while (top > 0) {
            int j = stack[top--];
            int i = stack[top--];
            pivotindex = (i + j) / 2;
            pivot = array[pivotindex];
            tmp = array[pivotindex];
            array[pivotindex] = array[j];
            array[j] = tmp;
            l = i - 1;
            r = j;
            do {
                while (comp.lt(array[++l], pivot)) ;
                while ((r != 0) && (comp.gt(array[--r], pivot))) ;
                tmp = array[l];
                array[l] = array[r];
                array[r] = tmp;
            } while (l < r);
            tmp = array[l];
            array[l] = array[r];
            array[r] = tmp;
            tmp = array[l];
            array[l] = array[j];
            array[j] = tmp;
            if ((l - i) > THRESHOLD) {
                stack[++top] = i;
                stack[++top] = l - 1;
            }
            if ((j - l) > THRESHOLD) {
                stack[++top] = l + 1;
                stack[++top] = j;
            }
        }
        inssort(array, comp);
    }
