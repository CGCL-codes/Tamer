    RangeNode build1D(Point[] A, int start, int end) {
        int mid = (start + end) / 2;
        RangeNode tmp = new RangeNode(A[mid]);
        tmp.setElement(A[mid].get(1));
        tmp.setLeafCount(end - start + 1);
        if (start != end) {
            tmp.setLeft(build1D(A, start, mid));
            tmp.setRight(build1D(A, mid + 1, end));
        }
        return tmp;
    }
