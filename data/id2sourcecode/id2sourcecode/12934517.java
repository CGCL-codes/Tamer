    public static Object3D extrudeCurve(Curve profile, Curve path, CoordinateSystem profCoords, CoordinateSystem pathCoords, double angle, boolean orient) {
        MeshVertex profVert[] = profile.getVertices(), pathVert[] = path.getVertices();
        Vec3 profv[] = new Vec3[profVert.length], pathv[] = new Vec3[pathVert.length];
        Vec3 subdiv[], center = new Vec3(), zdir[], updir[], t[], v[][];
        float usmooth[] = new float[pathVert.length], vsmooth[] = new float[profVert.length];
        float profSmooth[] = profile.getSmoothness(), pathSmooth[] = path.getSmoothness();
        CoordinateSystem localCoords = new CoordinateSystem(new Vec3(), Vec3.vz(), Vec3.vy());
        Mat4 rotate;
        int i, j;
        for (i = 0; i < profVert.length; i++) profv[i] = profCoords.fromLocal().timesDirection(profVert[i].r);
        for (i = 0; i < pathVert.length; i++) pathv[i] = pathCoords.fromLocal().timesDirection(pathVert[i].r);
        subdiv = new Curve(pathv, pathSmooth, path.getSmoothingMethod(), path.isClosed()).subdivideCurve().getVertexPositions();
        t = new Vec3[subdiv.length];
        zdir = new Vec3[subdiv.length];
        updir = new Vec3[subdiv.length];
        t[0] = subdiv[1].minus(subdiv[0]);
        t[0].normalize();
        zdir[0] = Vec3.vz();
        updir[0] = Vec3.vy();
        Vec3 dir1, dir2;
        double zfrac1, zfrac2, upfrac1, upfrac2;
        zfrac1 = t[0].dot(zdir[0]);
        zfrac2 = Math.sqrt(1.0 - zfrac1 * zfrac1);
        dir1 = zdir[0].minus(t[0].times(zfrac1));
        dir1.normalize();
        upfrac1 = t[0].dot(updir[0]);
        upfrac2 = Math.sqrt(1.0 - upfrac1 * upfrac1);
        dir2 = updir[0].minus(t[0].times(upfrac1));
        dir2.normalize();
        for (i = 1; i < subdiv.length; i++) {
            if (i == subdiv.length - 1) {
                if (path.isClosed()) t[i] = subdiv[0].minus(subdiv[subdiv.length - 2]); else t[i] = subdiv[subdiv.length - 1].minus(subdiv[subdiv.length - 2]);
            } else t[i] = subdiv[i + 1].minus(subdiv[i - 1]);
            t[i].normalize();
            if (orient) {
                dir1 = dir1.minus(t[i].times(t[i].dot(dir1)));
                dir1.normalize();
                dir2 = dir2.minus(t[i].times(t[i].dot(dir2)));
                dir2.normalize();
                zdir[i] = t[i].times(zfrac1).plus(dir1.times(zfrac2));
                updir[i] = t[i].times(upfrac1).plus(dir2.times(upfrac2));
            } else {
                zdir[i] = zdir[i - 1];
                updir[i] = updir[i - 1];
            }
        }
        if (path.getSmoothingMethod() != Mesh.NO_SMOOTHING) for (i = 0; i < usmooth.length; i++) usmooth[i] = pathSmooth[i];
        if (profile.getSmoothingMethod() != Mesh.NO_SMOOTHING) for (i = 0; i < vsmooth.length; i++) vsmooth[i] = profSmooth[i];
        if (profile.getSmoothingMethod() == Mesh.APPROXIMATING && path.getSmoothingMethod() == Mesh.INTERPOLATING) {
            pathv = subdiv;
            usmooth = new float[pathv.length];
            for (i = 0; i < usmooth.length; i++) {
                if (i % 2 == 0) usmooth[i] = Math.min(pathSmooth[i / 2] * 2.0f, 1.0f); else usmooth[i] = 1.0f;
            }
        }
        if (profile.getSmoothingMethod() == Mesh.INTERPOLATING && path.getSmoothingMethod() == Mesh.APPROXIMATING) {
            profv = new Curve(profv, profSmooth, profile.getSmoothingMethod(), profile.isClosed()).subdivideCurve().getVertexPositions();
            vsmooth = new float[profv.length];
            for (i = 0; i < vsmooth.length; i++) {
                if (i % 2 == 0) vsmooth[i] = Math.min(profSmooth[i / 2] * 2.0f, 1.0f); else vsmooth[i] = 1.0f;
            }
        }
        v = new Vec3[pathv.length][profv.length];
        for (i = 0; i < pathv.length; i++) {
            localCoords.setOrigin(pathv[i]);
            int k = (pathv.length == subdiv.length ? i : 2 * i);
            localCoords.setOrientation(zdir[k], updir[k]);
            if (angle != 0.0) {
                rotate = Mat4.axisRotation(t[k], i * angle / (pathv.length - 1));
                localCoords.transformAxes(rotate);
            }
            for (j = 0; j < profv.length; j++) {
                v[i][j] = localCoords.fromLocal().times(profv[j]);
                center.add(v[i][j]);
            }
        }
        center.scale(1.0 / (profv.length * pathv.length));
        for (i = 0; i < pathv.length; i++) for (j = 0; j < profv.length; j++) v[i][j].subtract(center);
        SplineMesh mesh = new SplineMesh(v, usmooth, vsmooth, Math.max(profile.getSmoothingMethod(), path.getSmoothingMethod()), path.isClosed(), profile.isClosed());
        mesh.makeRightSideOut();
        return mesh;
    }
