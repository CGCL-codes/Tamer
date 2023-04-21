    public void subdivide(float x1, float y1, float tx1, float ty1, float x2, float y2, float tx2, float ty2, GeneralPath p) {
        float xm = (x1 + x2) / 2;
        float ym = (y1 + y2) / 2;
        float scale = getScale(xm, ym);
        float txm = transformX(xm, scale);
        float tym = transformY(ym, scale);
        if (dist2(txm - (tx1 + tx2) / 2, tym - (ty1 + ty2) / 2) > tolerance) {
            subdivide(x1, y1, tx1, ty2, xm, ym, txm, tym, p);
            p.lineTo(txm, tym);
            subdivide(xm, ym, txm, tym, x2, y2, tx2, ty2, p);
        } else {
            p.lineTo(tx2, ty2);
        }
    }
