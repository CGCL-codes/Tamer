    private void interpolatePrecisely(int iLower, int iUpper) {
        int dx = (int) Math.floor(txRaster[iUpper]) - (int) Math.floor(txRaster[iLower]);
        if (dx < 0) dx = -dx;
        float dy = (int) Math.floor(tyRaster[iUpper]) - (int) Math.floor(tyRaster[iLower]);
        if (dy < 0) dy = -dy;
        if ((dx + dy) <= 1) return;
        float tLower = tRaster[iLower];
        float tUpper = tRaster[iUpper];
        int iMid = allocRaster(true);
        for (int j = 4; --j >= 0; ) {
            float tMid = (tLower + tUpper) / 2;
            calcRotatedPoint(tMid, iMid, true);
            if (((int) Math.floor(txRaster[iMid]) == (int) Math.floor(txRaster[iLower])) && ((int) Math.floor(tyRaster[iMid]) == (int) Math.floor(tyRaster[iLower]))) {
                fp8IntensityUp[iLower] = (fp8IntensityUp[iLower] + fp8IntensityUp[iMid]) >>> 1;
                tLower = tMid;
            } else if (((int) Math.floor(txRaster[iMid]) == (int) Math.floor(txRaster[iUpper])) && ((int) Math.floor(tyRaster[iMid]) == (int) Math.floor(tyRaster[iUpper]))) {
                fp8IntensityUp[iUpper] = (fp8IntensityUp[iUpper] + fp8IntensityUp[iMid]) >>> 1;
                tUpper = tMid;
            } else {
                interpolatePrecisely(iLower, iMid);
                interpolatePrecisely(iMid, iUpper);
                return;
            }
        }
        txRaster[iMid] = txRaster[iLower];
        tyRaster[iMid] = tyRaster[iUpper];
    }
