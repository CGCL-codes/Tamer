    private void subdivideSurface(double tol, Vector vert, Vector norm, Vector face, Vector param) {
        Tube t = subdivideTube(tol);
        Vec3 pathv[] = new Vec3[t.vertex.length];
        for (int i = 0; i < pathv.length; i++) pathv[i] = t.vertex[i].r;
        int numParam = (texParam == null ? 0 : texParam.length);
        double tubeParamVal[][] = new double[t.vertex.length][numParam];
        for (int i = 0; i < numParam; i++) {
            if (t.paramValue[i] instanceof VertexParameterValue) {
                double val[] = ((VertexParameterValue) t.paramValue[i]).getValue();
                for (int j = 0; j < tubeParamVal.length; j++) tubeParamVal[j][i] = val[j];
            } else {
                double val = t.paramValue[i].getAverageValue();
                for (int j = 0; j < tubeParamVal.length; j++) tubeParamVal[j][i] = val;
            }
        }
        double max = 0.0;
        for (int i = 0; i < t.thickness.length; i++) if (t.thickness[i] > max) max = t.thickness[i];
        double r = 0.7 * max;
        int n = 0;
        if (r > tol) n = (int) Math.ceil(Math.PI / (Math.acos(1.0 - tol / r)));
        if (n < 3) n = 3;
        Vec3 subdiv[], zdir[], updir[], xdir[];
        subdiv = new Curve(pathv, t.smoothness, t.getSmoothingMethod(), t.closed).subdivideCurve().getVertexPositions();
        xdir = new Vec3[subdiv.length];
        zdir = new Vec3[subdiv.length];
        updir = new Vec3[subdiv.length];
        xdir[0] = subdiv[1].minus(subdiv[0]);
        xdir[0].normalize();
        if (Math.abs(xdir[0].y) > Math.abs(xdir[0].z)) zdir[0] = xdir[0].cross(Vec3.vz()); else zdir[0] = xdir[0].cross(Vec3.vy());
        zdir[0].normalize();
        updir[0] = xdir[0].cross(zdir[0]);
        Vec3 dir1, dir2;
        double zfrac1, zfrac2, upfrac1, upfrac2;
        zfrac1 = xdir[0].dot(zdir[0]);
        zfrac2 = Math.sqrt(1.0 - zfrac1 * zfrac1);
        dir1 = zdir[0].minus(xdir[0].times(zfrac1));
        dir1.normalize();
        upfrac1 = xdir[0].dot(updir[0]);
        upfrac2 = Math.sqrt(1.0 - upfrac1 * upfrac1);
        dir2 = updir[0].minus(xdir[0].times(upfrac1));
        dir2.normalize();
        for (int i = 1; i < subdiv.length; i++) {
            if (i == subdiv.length - 1) {
                if (t.closed) xdir[i] = subdiv[0].minus(subdiv[subdiv.length - 2]); else xdir[i] = subdiv[subdiv.length - 1].minus(subdiv[subdiv.length - 2]);
            } else xdir[i] = subdiv[i + 1].minus(subdiv[i - 1]);
            xdir[i].normalize();
            dir1 = dir1.minus(xdir[i].times(xdir[i].dot(dir1)));
            dir1.normalize();
            dir2 = dir2.minus(xdir[i].times(xdir[i].dot(dir2)));
            dir2.normalize();
            zdir[i] = xdir[i].times(zfrac1).plus(dir1.times(zfrac2));
            updir[i] = xdir[i].times(upfrac1).plus(dir2.times(upfrac2));
        }
        double dtheta = 2.0 * Math.PI / n, theta = 0.0;
        for (int i = 0; i < pathv.length; i++) {
            int k = (pathv.length == subdiv.length ? i : 2 * i);
            Vec3 orig = pathv[i], z = zdir[k], up = updir[k];
            r = 0.5 * t.thickness[i];
            for (int j = 0; j < n; j++) {
                double sin = Math.sin(theta), cos = Math.cos(theta);
                Vec3 normal = new Vec3(cos * z.x + sin * up.x, cos * z.y + sin * up.y, cos * z.z + sin * up.z);
                norm.addElement(normal);
                MeshVertex mv = new MeshVertex(new Vec3(orig.x + r * normal.x, orig.y + r * normal.y, orig.z + r * normal.z));
                vert.addElement(mv);
                param.addElement(tubeParamVal[i]);
                theta += dtheta;
            }
        }
        for (int i = 0; i < pathv.length - 1; i++) {
            int k = i * n;
            for (int j = 0; j < n - 1; j++) {
                face.addElement(new int[] { k + j, k + j + 1, k + j + n });
                face.addElement(new int[] { k + j + 1, k + j + n + 1, k + j + n });
            }
            face.addElement(new int[] { k + n - 1, k, k + n + n - 1 });
            face.addElement(new int[] { k, k + n, k + n + n - 1 });
        }
        if (endsStyle == CLOSED_ENDS) {
            int k = (pathv.length - 1) * n;
            for (int j = 0; j < n - 1; j++) {
                face.addElement(new int[] { k + j, k + j + 1, j });
                face.addElement(new int[] { k + j + 1, j + 1, j });
            }
            face.addElement(new int[] { k + n - 1, k, n - 1 });
            face.addElement(new int[] { k, 0, n - 1 });
        } else if (endsStyle == FLAT_ENDS) {
            int k = vert.size();
            vert.addElement(new MeshVertex(t.vertex[0]));
            vert.addElement(new MeshVertex(t.vertex[t.vertex.length - 1]));
            param.addElement(tubeParamVal[0]);
            param.addElement(tubeParamVal[t.vertex.length - 1]);
            for (int i = 0; i < n - 1; i++) face.addElement(new int[] { i + 1, i, k });
            face.addElement(new int[] { 0, n - 1, k });
            k++;
            int j = n * (pathv.length - 1);
            for (int i = 0; i < n - 1; i++) face.addElement(new int[] { j + i, j + i + 1, k });
            face.addElement(new int[] { j + n - 1, j, k });
        }
    }
