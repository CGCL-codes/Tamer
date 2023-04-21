    private static double[] triangleCentroid(double[][] verts) {
        double[] centroid_xy = new double[2];
        double[][] midpoint_12 = new double[2][1];
        double[][] midpoint_13 = new double[2][1];
        double[][] verts_xy = new double[2][3];
        verts_xy[0][0] = verts[0][0];
        verts_xy[0][1] = verts[0][1];
        verts_xy[0][2] = verts[0][2];
        verts_xy[1][0] = verts[1][0];
        verts_xy[1][1] = verts[1][1];
        verts_xy[1][2] = verts[1][2];
        double slope_12_3;
        double slope_13_2;
        double slope_23_1;
        double yintercept_12_3;
        double yintercept_13_2;
        double yintercept_23_1;
        boolean rotate = false;
        double rot_angle;
        midpoint_12[0][0] = (verts_xy[0][1] - verts_xy[0][0]) / 2 + verts_xy[0][0];
        midpoint_12[1][0] = (verts_xy[1][1] - verts_xy[1][0]) / 2 + verts_xy[1][0];
        midpoint_13[0][0] = (verts_xy[0][2] - verts_xy[0][0]) / 2 + verts_xy[0][0];
        midpoint_13[1][0] = (verts_xy[1][2] - verts_xy[1][0]) / 2 + verts_xy[1][0];
        slope_12_3 = (verts_xy[1][2] - midpoint_12[1][0]) / (verts_xy[0][2] - midpoint_12[0][0]);
        slope_13_2 = (verts_xy[1][1] - midpoint_13[1][0]) / (verts_xy[0][1] - midpoint_13[0][0]);
        if (Double.isInfinite(slope_12_3) || Double.isInfinite(slope_13_2)) {
            System.out.println("infinite slope");
            rotate_clockwise(verts_xy, 90 * Data.DEGREES_TO_RADIANS);
            rotate_clockwise(midpoint_12, 90 * Data.DEGREES_TO_RADIANS);
            rotate_clockwise(midpoint_13, 90 * Data.DEGREES_TO_RADIANS);
            rotate = true;
        }
        yintercept_12_3 = verts_xy[1][2] - slope_12_3 * verts_xy[0][2];
        yintercept_13_2 = verts_xy[1][1] - slope_13_2 * verts_xy[0][1];
        centroid_xy[0] = (yintercept_12_3 - yintercept_13_2) / (slope_13_2 - slope_12_3);
        centroid_xy[1] = slope_12_3 * centroid_xy[0] + yintercept_12_3;
        if (rotate == true) {
            double[][] xy = new double[2][1];
            xy[0][0] = centroid_xy[0];
            xy[1][0] = centroid_xy[1];
            rotate_clockwise(xy, -90 * Data.DEGREES_TO_RADIANS);
            centroid_xy[0] = xy[0][0];
            centroid_xy[1] = xy[1][0];
        }
        return centroid_xy;
    }
