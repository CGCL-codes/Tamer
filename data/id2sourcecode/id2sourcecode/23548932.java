    private void interpolate(int iLower, int iUpper) {
        int dx = xRaster[iUpper] - xRaster[iLower];
        if (dx < 0) dx = -dx;
        int dy = yRaster[iUpper] - yRaster[iLower];
        if (dy < 0) dy = -dy;
        if ((dx + dy) <= 1) return;
        float tLower = tRaster[iLower];
        float tUpper = tRaster[iUpper];
        int iMid = allocRaster(false);
        for (int j = 4; --j >= 0; ) {
            float tMid = (tLower + tUpper) / 2;
            calcRotatedPoint(tMid, iMid, false);
            if ((xRaster[iMid] == xRaster[iLower]) && (yRaster[iMid] == yRaster[iLower])) {
                fp8IntensityUp[iLower] = (fp8IntensityUp[iLower] + fp8IntensityUp[iMid]) >>> 1;
                tLower = tMid;
            } else if ((xRaster[iMid] == xRaster[iUpper]) && (yRaster[iMid] == yRaster[iUpper])) {
                fp8IntensityUp[iUpper] = (fp8IntensityUp[iUpper] + fp8IntensityUp[iMid]) >>> 1;
                tUpper = tMid;
            } else {
                interpolate(iLower, iMid);
                interpolate(iMid, iUpper);
                return;
            }
        }
        xRaster[iMid] = xRaster[iLower];
        yRaster[iMid] = yRaster[iUpper];
    }
