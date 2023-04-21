    public static final void quicksort(char[] array) {
        int start = 0;
        int end = -1;
        Stack stack = new Stack();
        int low;
        int high;
        Range range;
        int pivot;
        char pivotVal;
        char tmpVal;
        stack.push(new Range(0, array.length - 1));
        while (stack.size() > 0) {
            if (start >= end) {
                range = (Range) stack.pop();
                start = range.start;
                end = range.end;
            }
            if (start < end) {
                pivot = start + (end - start) / 2;
                if ((end - start) > 7) pivot = ((array[start] < array[pivot]) ? ((array[pivot] < array[end]) ? pivot : ((array[start] < array[end]) ? end : start)) : ((array[pivot] > array[end]) ? pivot : ((array[start] > array[end]) ? end : start)));
                pivotVal = array[pivot];
                low = start;
                high = end;
                while (low < high) {
                    while ((array[low] < pivotVal) && (low <= end)) low++;
                    while ((array[high] > pivotVal) && (high >= start)) high--;
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
