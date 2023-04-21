    private static void inPlaceSort(String arr[], String buf[], int a, int b) {
        if (b - a <= 1) {
            return;
        }
        int c = (b + a) / 2;
        inPlaceSort(arr, buf, a, c);
        inPlaceSort(arr, buf, c, b);
        int s1 = a;
        int s2 = c;
        int i = a;
        while ((s1 < c) && (s2 < b)) {
            if (arr[s1].compareTo(arr[s2]) <= 0) {
                buf[i++] = arr[s1++];
            } else {
                buf[i++] = arr[s2++];
            }
        }
        while (s1 < c) {
            buf[i++] = arr[s1++];
        }
        while (s2 < b) {
            buf[i++] = arr[s2++];
        }
        for (int j = a; j < b; j++) {
            arr[j] = buf[j];
        }
    }
