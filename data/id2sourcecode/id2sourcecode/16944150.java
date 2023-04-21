    private void QuickSort(float data[], int l, int r) throws Exception {
        int M = 4;
        int i;
        int j;
        float v;
        if ((r - l) > M) {
            i = (r + l) / 2;
            if (data[l] > data[i]) {
                swap(data, l, i);
            }
            if (data[l] > data[r]) {
                swap(data, l, r);
            }
            if (data[i] > data[r]) {
                swap(data, i, r);
            }
            j = r - 1;
            swap(data, i, j);
            i = l;
            v = data[j];
            for (; ; ) {
                while (data[++i] < v) {
                    ;
                }
                while (data[--j] > v) {
                    ;
                }
                if (j < i) {
                    break;
                }
                swap(data, i, j);
            }
            swap(data, i, r - 1);
            QuickSort(data, l, j);
            QuickSort(data, i + 1, r);
        }
    }
