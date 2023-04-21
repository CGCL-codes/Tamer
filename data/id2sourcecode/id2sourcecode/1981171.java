    private VisADLineArray makeTrack(float[][] values) {
        float d, xd, yd;
        float x, y, z, x0, y0, x3, y3, x4, y4, x5, y5;
        float sscale = 0.75f * 0.15f;
        x = 0.0f;
        y = 0.0f;
        z = values[2][0];
        x5 = values[0][1] - values[0][0];
        y5 = values[1][1] - values[1][0];
        float xdir = x5 - x;
        float ydir = y5 - y;
        float dist = (float) Math.sqrt(xdir * xdir + ydir * ydir);
        x0 = xdir / dist;
        y0 = ydir / dist;
        int nv = 0;
        float[] vx = new float[NUM];
        float[] vy = new float[NUM];
        float[] vz = new float[NUM];
        int lenv = vx.length;
        vx[nv] = x;
        vy[nv] = y;
        vz[nv] = z;
        nv++;
        vx[nv] = x5;
        vy[nv] = y5;
        vz[nv] = z;
        nv++;
        xd = sscale * x0;
        yd = sscale * y0;
        x3 = x5 - 0.3f * (xd - yd);
        y3 = y5 - 0.3f * (yd + xd);
        x4 = x5 - 0.3f * (xd + yd);
        y4 = y5 - 0.3f * (yd - xd);
        vx[nv] = x5;
        vy[nv] = y5;
        vz[nv] = z;
        nv++;
        vx[nv] = x3;
        vy[nv] = y3;
        vz[nv] = z;
        nv++;
        vx[nv] = x5;
        vy[nv] = y5;
        vz[nv] = z;
        nv++;
        vx[nv] = x4;
        vy[nv] = y4;
        vz[nv] = z;
        nv++;
        float step = getStep(xdir, ydir);
        int nsteps = (int) (dist / step);
        if (nsteps < 1) nsteps = 1;
        int lim = (vx.length - nv) / (2 * NE);
        if (nsteps < 1) nsteps = 1;
        if (nsteps > lim) nsteps = lim;
        float xstep = xdir / nsteps;
        float ystep = ydir / nsteps;
        boolean[] outside = new boolean[NE + 1];
        for (int i = 0; i < NE + 1; i++) {
            float xs = x_ellipse[i] + xstep;
            float ys = y_ellipse[i] + ystep;
            float radius = getStep(xs, ys);
            float len = (float) Math.sqrt(xs * xs + ys * ys);
            outside[i] = (len > radius);
        }
        float[] xe = new float[2 * NE];
        float[] ye = new float[2 * NE];
        int ne = 0;
        for (int i = 0; i < NE; i++) {
            if (outside[i] && outside[i + 1]) {
                xe[ne] = x_ellipse[i];
                ye[ne] = y_ellipse[i];
                ne++;
                xe[ne] = x_ellipse[i + 1];
                ye[ne] = y_ellipse[i + 1];
                ne++;
            }
        }
        float xcenter = x;
        float ycenter = y;
        for (int i = 0; i < NE; i++) {
            vx[nv] = x_ellipse[i];
            vy[nv] = y_ellipse[i];
            vz[nv] = z;
            nv++;
            vx[nv] = x_ellipse[i + 1];
            vy[nv] = y_ellipse[i + 1];
            vz[nv] = z;
            nv++;
        }
        for (int i = 0; i < nsteps; i++) {
            xcenter += xstep;
            ycenter += ystep;
            for (int j = 0; j < ne; j++) {
                vx[nv] = xcenter + xe[j];
                vy[nv] = ycenter + ye[j];
                vz[nv] = z;
                nv++;
            }
        }
        VisADLineArray array = new VisADLineArray();
        array.vertexCount = nv;
        float[] coordinates = new float[3 * nv];
        int m = 0;
        for (int i = 0; i < nv; i++) {
            coordinates[m++] = vx[i];
            coordinates[m++] = vy[i];
            coordinates[m++] = vz[i];
        }
        array.coordinates = coordinates;
        return array;
    }
