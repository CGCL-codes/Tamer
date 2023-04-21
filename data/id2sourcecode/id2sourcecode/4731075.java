    private void recursiveAdd(int start, int end, E[] arr) {
        if (end < start) return;
        if (end == start) {
            add(arr[end]);
            return;
        }
        if (end - start == 1) {
            add(arr[start]);
            add(arr[end]);
            return;
        }
        int mid = (start + end) / 2;
        add(arr[mid]);
        recursiveAdd(start, mid - 1, arr);
        recursiveAdd(mid + 1, end, arr);
    }
