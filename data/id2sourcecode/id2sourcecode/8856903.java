    public static void perspectiveTransform(double[] src, double[] dst, CvMat map_matrix) {
        double[] mat = map_matrix.get();
        for (int j = 0; j < src.length; j += 2) {
            double x = src[j], y = src[j + 1];
            double w = x * mat[6] + y * mat[7] + mat[8];
            if (Math.abs(w) > FLT_EPSILON) {
                w = 1.0 / w;
                dst[j] = (x * mat[0] + y * mat[1] + mat[2]) * w;
                dst[j + 1] = (x * mat[3] + y * mat[4] + mat[5]) * w;
            } else {
                dst[j] = dst[j + 1] = 0;
            }
        }
    }
