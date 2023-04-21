    private void worldCasteljauBezierSubDivision(double p0x, double p0y, double p1x, double p1y, double p2x, double p2y, double p3x, double p3y, int depth) {
        double q0x;
        double q0y;
        double q1x;
        double q1y;
        double q2x;
        double q2y;
        double r0x;
        double r0y;
        double r1x;
        double r1y;
        double r2x;
        double r2y;
        if (depth == 0) {
            worldPoints.addPoint(p3x, p3y);
        } else {
            q0x = (p0x + p1x) / 2;
            q0y = (p0y + p1y) / 2;
            q1x = (p1x + p2x) / 2;
            q1y = (p1y + p2y) / 2;
            q2x = (p2x + p3x) / 2;
            q2y = (p2y + p3y) / 2;
            r0x = (q0x + q1x) / 2;
            r0y = (q0y + q1y) / 2;
            r1x = (q1x + q2x) / 2;
            r1y = (q1y + q2y) / 2;
            r2x = (r0x + r1x) / 2;
            r2y = (r0y + r1y) / 2;
            worldCasteljauBezierSubDivision(p0x, p0y, q0x, q0y, r0x, r0y, r2x, r2y, depth - 1);
            worldCasteljauBezierSubDivision(r2x, r2y, r1x, r1y, q2x, q2y, p3x, p3y, depth - 1);
        }
    }
