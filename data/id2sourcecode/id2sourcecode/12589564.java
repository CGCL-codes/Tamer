    public double indexOf(double x) {
        int size = points.size();
        DoublePoint pt1, pt2, pt3;
        int index1, index2, index3;
        if (size == 0) return Double.POSITIVE_INFINITY;
        pt1 = (DoublePoint) points.firstElement();
        pt2 = (DoublePoint) points.lastElement();
        if (pt1.x > x) return Double.NEGATIVE_INFINITY;
        if (pt2.x < x) return Double.POSITIVE_INFINITY;
        index1 = 0;
        index2 = size - 1;
        while (index1 + 1 < index2) {
            index3 = (index1 + index2) / 2;
            pt3 = (DoublePoint) points.elementAt(index3);
            if (pt3.x > x) {
                pt2 = pt3;
                index2 = index3;
            } else {
                pt1 = pt3;
                index1 = index3;
            }
        }
        if (pt1.x != pt2.x) {
            return ((double) index1 + (x - pt1.x) / (pt2.x - pt1.x));
        } else {
            return ((double) index1);
        }
    }
