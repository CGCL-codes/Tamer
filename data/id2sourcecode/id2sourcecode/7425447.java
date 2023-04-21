    protected void mergeSort(int begin, int end) {
        if (begin != end) {
            int mid = (begin + end) / 2;
            this.mergeSort(begin, mid);
            this.mergeSort(mid + 1, end);
            this.merge(begin, mid, end);
        }
    }
