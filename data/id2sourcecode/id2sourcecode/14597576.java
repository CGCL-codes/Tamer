    private void casteljauBezierSubDivision(int p0x, int p0y, int p1x, int p1y, int p2x, int p2y, int p3x, int p3y, int depth) {
        int q0x;
        int q0y;
        int q1x;
        int q1y;
        int q2x;
        int q2y;
        int r0x;
        int r0y;
        int r1x;
        int r1y;
        int r2x;
        int r2y;
        if (depth == 0) {
            points.addPoint(p3x, p3y);
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
            casteljauBezierSubDivision(p0x, p0y, q0x, q0y, r0x, r0y, r2x, r2y, depth - 1);
            casteljauBezierSubDivision(r2x, r2y, r1x, r1y, q2x, q2y, p3x, p3y, depth - 1);
        }
    }
