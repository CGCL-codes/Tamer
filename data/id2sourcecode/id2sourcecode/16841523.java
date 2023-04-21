    public static final void quicksort(Object[] array, Comparator comp) {
        int start = 0;
        int end = -1;
        Stack stack = new Stack();
        int low;
        int high;
        Range range;
        int pivot;
        Object pivotVal;
        Object startVal;
        Object endVal;
        Object tmpVal;
        stack.push(new Range(0, array.length - 1));
        while (stack.size() > 0) {
            if (start >= end) {
                range = (Range) stack.pop();
                start = range.start;
                end = range.end;
            }
            if (start < end) {
                pivot = start + (end - start) / 2;
                if ((end - start) > 7) {
                    startVal = array[start];
                    endVal = array[end];
                    pivotVal = array[pivot];
                    pivot = ((comp.compare(startVal, pivotVal) < 0) ? ((comp.compare(pivotVal, endVal) < 0) ? pivot : ((comp.compare(startVal, endVal) < 0) ? end : start)) : ((comp.compare(pivotVal, endVal) > 0) ? pivot : ((comp.compare(startVal, endVal) > 0) ? end : start)));
                }
                pivotVal = array[pivot];
                low = start;
                high = end;
                while (low < high) {
                    while ((comp.compare(array[low], pivotVal) < 0) && (low <= end)) low++;
                    while ((comp.compare(array[high], pivotVal) > 0) && (high >= start)) high--;
                    if (low <= high) {
                        if (low < high) {
                            tmpVal = array[low];
                            array[low] = array[high];
                            array[high] = tmpVal;
                        }
                        low++;
                        high--;
                    }
                }
                if ((high - start) > (end - low)) {
                    stack.push(new Range(low, end));
                    end = high;
                } else {
                    stack.push(new Range(start, high));
                    start = low;
                }
            }
        }
    }
