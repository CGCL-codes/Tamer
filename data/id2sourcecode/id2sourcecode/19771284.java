    public static Shape arrow(float bx, float by, float tx, float ty, float cx, float cy) {
        float dx = tx - bx;
        float dy = ty - by;
        float ix = dx * cx;
        float iy = dy * cy;
        float near = bx + ix;
        float far = tx - ix;
        float peakx = (bx + tx) / 2;
        float[] verts = new float[] { near, by, near, iy, bx, iy, peakx, ty, tx, iy, far, iy, far, bx };
        int[] tris = new int[] { 1, 3, 2, 1, 5, 3, 5, 4, 3, 0, 5, 1, 0, 6, 5 };
        return new Shape(to3D(verts, 0), tris);
    }
