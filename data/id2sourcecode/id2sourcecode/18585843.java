    protected void transform() {
        if (transformed || nvert <= 0) return;
        if (tvert == null || tvert.length < nvert * 3) tvert = new int[nvert * 3];
        float temp[] = new float[4];
        for (int i = nvert * 3; (i -= 3) >= 0; ) {
            temp[0] = vert[i];
            temp[1] = vert[i + 1];
            temp[2] = vert[i + 2];
            temp[3] = 1.0f;
            float[] newVert = mat.mult(temp);
            tvert[i] = (int) newVert[0];
            tvert[i + 1] = (int) newVert[1];
            tvert[i + 2] = (int) newVert[2];
            transformed = true;
        }
    }
