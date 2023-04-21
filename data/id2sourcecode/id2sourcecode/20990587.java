    public Matrix33 affineInverse() {
        float Tx, Ty;
        Matrix33 result = new Matrix33();
        result._m[0][0] = _m[0][0];
        result._m[0][1] = _m[1][0];
        result._m[1][0] = _m[0][1];
        result._m[1][1] = _m[1][1];
        result._m[2][0] = 0f;
        result._m[2][1] = 0f;
        result._m[2][2] = 1f;
        Tx = _m[0][2];
        Ty = _m[1][2];
        result._m[0][2] = -(_m[0][0] * Tx + _m[1][0] * Ty);
        result._m[1][2] = -(_m[0][1] * Tx + _m[1][1] * Ty);
        return result;
    }
