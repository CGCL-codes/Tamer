    ColorNode build1D(RangeInTree[] A, int start, int end) {
        if (A.length == 0) return null;
        int mid = (start + end) / 2;
        ColorNode tmp = new ColorNode(A[mid], null, null);
        if (end - start >= 2) tmp.setLeft(build1D(A, start, mid - 1));
        if (end - start >= 1) tmp.setRight(build1D(A, mid + 1, end));
        return tmp;
    }
