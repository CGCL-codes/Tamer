    public static void subdivide(double[] src, int srcOff, double[] left, int leftOff, double[] right, int rightOff) {
        double x1;
        double y1;
        double xc;
        double yc;
        double x2;
        double y2;
        x1 = src[srcOff];
        y1 = src[srcOff + 1];
        xc = src[srcOff + 2];
        yc = src[srcOff + 3];
        x2 = src[srcOff + 4];
        y2 = src[srcOff + 5];
        if (left != null) {
            left[leftOff] = x1;
            left[leftOff + 1] = y1;
        }
        if (right != null) {
            right[rightOff + 4] = x2;
            right[rightOff + 5] = y2;
        }
        x1 = (x1 + xc) / 2;
        x2 = (xc + x2) / 2;
        xc = (x1 + x2) / 2;
        y1 = (y1 + yc) / 2;
        y2 = (y2 + yc) / 2;
        yc = (y1 + y2) / 2;
        if (left != null) {
            left[leftOff + 2] = x1;
            left[leftOff + 3] = y1;
            left[leftOff + 4] = xc;
            left[leftOff + 5] = yc;
        }
        if (right != null) {
            right[rightOff] = xc;
            right[rightOff + 1] = yc;
            right[rightOff + 2] = x2;
            right[rightOff + 3] = y2;
        }
    }
