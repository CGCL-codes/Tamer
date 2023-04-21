    public static void sort(Object[] arr, int start, int end) {
        if (end - start <= 2) {
            if (end - start == 2 && arr[start].toString().compareTo(arr[start + 1].toString()) > 0) {
                Object tmp = arr[start];
                arr[start] = arr[start + 1];
                arr[start + 1] = tmp;
            }
            return;
        }
        if (end - start == 3) {
            sort(arr, start, start + 2);
            sort(arr, start + 1, start + 3);
            sort(arr, start, start + 2);
            return;
        }
        int middle = (start + end) / 2;
        sort(arr, start, middle);
        sort(arr, middle, end);
        Object[] tmp = new Object[end - start];
        int i0 = start;
        int i1 = middle;
        for (int i = 0; i < tmp.length; i++) {
            if (i0 == middle) {
                tmp[i] = arr[i1++];
            } else if (i1 == end || arr[i0].toString().compareTo(arr[i1].toString()) < 0) {
                tmp[i] = arr[i0++];
            } else {
                tmp[i] = arr[i1++];
            }
        }
        System.arraycopy(tmp, 0, arr, start, tmp.length);
    }
