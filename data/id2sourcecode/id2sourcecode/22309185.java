    public RangeNode build2D(Point[] A, int start, int end) {
        int mid = (start + end) / 2;
        Point[] B = new Point[end - start + 1];
        for (int i = start; i <= end; i++) B[i - start] = (Point) A[i].clone();
        mergeSort(B, 1);
        RangeNode tmp = new RangeNode(new RangeTree(B, 1));
        tmp.setElement(A[mid].get(0));
        if (start != end) {
            tmp.setLeft(build2D(A, start, mid));
            tmp.setRight(build2D(A, mid + 1, end));
        }
        return tmp;
    }
