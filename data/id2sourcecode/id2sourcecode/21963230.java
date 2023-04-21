        this(iaKey.length);
        size = iaKey.length;
        for (int i = 0; i < size; i++) {
            data[i] = iaKey[i];
        }
    }

    public void dump(PrintStream out) {
        if (out == null) {
            out = System.out;
        }
        out.println("(");
        for (int i = 0; i < size(); i++) {
            out.print(at(i) + " ");
        }
        out.println(")");
    }

    private void QuickSort(int data[], int l, int r) throws Exception {
        int M = 4;
        int i;
        int j;
        int v;
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
