    public static void subdivide(double[] src, int srcOff, double[] left, int leftOff, double[] right, int rightOff) {
        double src_C1_x;
        double src_C1_y;
        double src_C2_x;
        double src_C2_y;
        double left_P1_x;
        double left_P1_y;
        double left_C1_x;
        double left_C1_y;
        double left_C2_x;
        double left_C2_y;
        double right_C1_x;
        double right_C1_y;
        double right_C2_x;
        double right_C2_y;
        double right_P2_x;
        double right_P2_y;
        double Mid_x;
        double Mid_y;
        left_P1_x = src[srcOff];
        left_P1_y = src[srcOff + 1];
        src_C1_x = src[srcOff + 2];
        src_C1_y = src[srcOff + 3];
        src_C2_x = src[srcOff + 4];
        src_C2_y = src[srcOff + 5];
        right_P2_x = src[srcOff + 6];
        right_P2_y = src[srcOff + 7];
        left_C1_x = (left_P1_x + src_C1_x) / 2;
        left_C1_y = (left_P1_y + src_C1_y) / 2;
        right_C2_x = (right_P2_x + src_C2_x) / 2;
        right_C2_y = (right_P2_y + src_C2_y) / 2;
        Mid_x = (src_C1_x + src_C2_x) / 2;
        Mid_y = (src_C1_y + src_C2_y) / 2;
        left_C2_x = (left_C1_x + Mid_x) / 2;
        left_C2_y = (left_C1_y + Mid_y) / 2;
        right_C1_x = (Mid_x + right_C2_x) / 2;
        right_C1_y = (Mid_y + right_C2_y) / 2;
        Mid_x = (left_C2_x + right_C1_x) / 2;
        Mid_y = (left_C2_y + right_C1_y) / 2;
        if (left != null) {
            left[leftOff] = left_P1_x;
            left[leftOff + 1] = left_P1_y;
            left[leftOff + 2] = left_C1_x;
            left[leftOff + 3] = left_C1_y;
            left[leftOff + 4] = left_C2_x;
            left[leftOff + 5] = left_C2_y;
            left[leftOff + 6] = Mid_x;
            left[leftOff + 7] = Mid_y;
        }
        if (right != null) {
            right[rightOff] = Mid_x;
            right[rightOff + 1] = Mid_y;
            right[rightOff + 2] = right_C1_x;
            right[rightOff + 3] = right_C1_y;
            right[rightOff + 4] = right_C2_x;
            right[rightOff + 5] = right_C2_y;
            right[rightOff + 6] = right_P2_x;
            right[rightOff + 7] = right_P2_y;
        }
    }
