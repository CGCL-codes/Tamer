    public static Object3D extrudeMesh(TriangleMesh profile, Curve path, CoordinateSystem profCoords, CoordinateSystem pathCoords, double angle, boolean orient) {
        Vertex profVert[] = (Vertex[]) profile.getVertices();
        MeshVertex pathVert[] = path.getVertices();
        Edge profEdge[] = profile.getEdges();
        Face profFace[] = profile.getFaces();
        Vec3 profv[] = new Vec3[profVert.length], pathv[] = new Vec3[pathVert.length];
        Vec3 subdiv[], center, zdir[], updir[], t[], v[];
        float pathSmooth[] = path.getSmoothness();
        CoordinateSystem localCoords = new CoordinateSystem(new Vec3(), Vec3.vz(), Vec3.vy());
        Mat4 rotate;
        int numBoundaryEdges = 0, numBoundaryPoints = 0, i, j, k;
        int boundaryEdge[], boundaryPoint[];
        for (i = 0; i < profVert.length; i++) profv[i] = profCoords.fromLocal().timesDirection(profVert[i].r);
        for (i = 0; i < pathVert.length; i++) pathv[i] = pathCoords.fromLocal().timesDirection(pathVert[i].r);
        if (path.getSmoothingMethod() == Mesh.NO_SMOOTHING) for (i = 0; i < pathSmooth.length; i++) pathSmooth[i] = 0.0f;
        boolean onBound[] = new boolean[profv.length];
        for (i = 0; i < profEdge.length; i++) if (profEdge[i].f2 == -1) {
            numBoundaryEdges++;
            onBound[profEdge[i].v1] = onBound[profEdge[i].v2] = true;
        }
        for (i = 0; i < onBound.length; i++) if (onBound[i]) numBoundaryPoints++;
        boundaryEdge = new int[numBoundaryEdges];
        boundaryPoint = new int[numBoundaryPoints];
        for (i = 0, j = 0; i < profEdge.length; i++) if (profEdge[i].f2 == -1) boundaryEdge[j++] = i;
        for (i = 0, j = 0; i < onBound.length; i++) if (onBound[i]) boundaryPoint[j++] = i;
        boolean forward[] = new boolean[boundaryEdge.length];
        int edgeVertIndex[][] = new int[boundaryEdge.length][2];
        for (i = 0; i < boundaryEdge.length; i++) {
            Edge ed = profEdge[boundaryEdge[i]];
            Face fc = profFace[ed.f1];
            forward[i] = ((fc.v1 == ed.v1 && fc.v2 == ed.v2) || (fc.v2 == ed.v1 && fc.v3 == ed.v2) || (fc.v3 == ed.v1 && fc.v1 == ed.v2));
            for (j = 0; j < boundaryPoint.length; j++) {
                if (boundaryPoint[j] == ed.v1) edgeVertIndex[i][0] = j; else if (boundaryPoint[j] == ed.v2) edgeVertIndex[i][1] = j;
            }
        }
        int index[][];
        if (path.isClosed()) {
            index = new int[pathv.length + 1][boundaryPoint.length];
            for (i = 0; i < boundaryPoint.length; i++) {
                for (j = 0; j < pathv.length; j++) index[j][i] = j * boundaryPoint.length + i;
                index[j][i] = i;
            }
        } else {
            index = new int[pathv.length][boundaryPoint.length];
            for (i = 0; i < boundaryPoint.length; i++) {
                index[0][i] = boundaryPoint[i];
                index[pathv.length - 1][i] = boundaryPoint[i] + profv.length;
                for (j = 1; j < pathv.length - 1; j++) index[j][i] = (j - 1) * boundaryPoint.length + i + 2 * profv.length;
            }
        }
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
        if (path.isClosed()) v = new Vec3[numBoundaryPoints * pathv.length]; else v = new Vec3[2 * profv.length + numBoundaryPoints * (pathv.length - 2)];
        Vector newEdge = new Vector(), newFace = new Vector();
        boolean angled = (profile.getSmoothingMethod() == Mesh.NO_SMOOTHING && path.getSmoothingMethod() != Mesh.NO_SMOOTHING);
        if (!path.isClosed()) {
            localCoords.setOrigin(pathv[0]);
            localCoords.setOrientation(zdir[0], updir[0]);
            for (i = 0; i < profv.length; i++) v[i] = localCoords.fromLocal().times(profv[i]);
            k = (pathv.length == subdiv.length ? pathv.length - 1 : 2 * (pathv.length - 1));
            localCoords.setOrigin(pathv[pathv.length - 1]);
            localCoords.setOrientation(zdir[k], updir[k]);
            if (angle != 0.0) {
                rotate = Mat4.axisRotation(t[k], angle);
                localCoords.transformAxes(rotate);
            }
            for (i = 0; i < profv.length; i++) v[i + profv.length] = localCoords.fromLocal().times(profv[i]);
            for (i = 0; i < profEdge.length; i++) {
                float smoothness = profEdge[i].smoothness;
                if (angled || profEdge[i].f2 == -1) smoothness = 0.0f;
                newEdge.addElement(new EdgeInfo(profEdge[i].v1, profEdge[i].v2, smoothness));
                newEdge.addElement(new EdgeInfo(profEdge[i].v1 + profv.length, profEdge[i].v2 + profv.length, smoothness));
            }
            for (i = 0; i < profFace.length; i++) {
                Face f = profFace[i];
                newFace.addElement(new int[] { f.v1, f.v2, f.v3 });
                newFace.addElement(new int[] { f.v1 + profv.length, f.v3 + profv.length, f.v2 + profv.length });
            }
        }
        for (i = 0; i < pathv.length; i++) {
            if (!path.isClosed() && i == pathv.length - 1) break;
            for (j = 0; j < boundaryEdge.length; j++) {
                int v1, v2;
                if (forward[j]) {
                    v1 = edgeVertIndex[j][0];
                    v2 = edgeVertIndex[j][1];
                } else {
                    v1 = edgeVertIndex[j][1];
                    v2 = edgeVertIndex[j][0];
                }
                newFace.addElement(new int[] { index[i][v1], index[i + 1][v1], index[i + 1][v2] });
                newFace.addElement(new int[] { index[i][v2], index[i][v1], index[i + 1][v2] });
                EdgeInfo ed1 = new EdgeInfo(index[i][v1], index[i + 1][v1], angled ? 0.0f : profVert[boundaryPoint[v1]].smoothness);
                newEdge.addElement(ed1);
                ed1 = new EdgeInfo(index[i][v2], index[i + 1][v2], angled ? 0.0f : profVert[boundaryPoint[v2]].smoothness);
                newEdge.addElement(ed1);
                ed1 = new EdgeInfo(index[i][v1], index[i + 1][v2], 1.0f);
                newEdge.addElement(ed1);
                if (path.isClosed() || i > 0) {
                    ed1 = new EdgeInfo(index[i][v1], index[i][v2], pathSmooth[i]);
                    newEdge.addElement(ed1);
                }
            }
            localCoords.setOrigin(pathv[i]);
            k = (pathv.length == subdiv.length ? i : 2 * i);
            localCoords.setOrientation(zdir[k], updir[k]);
            if (angle != 0.0) {
                rotate = Mat4.axisRotation(t[k], i * angle / (pathv.length - 1));
                localCoords.transformAxes(rotate);
            }
            for (j = 0; j < boundaryPoint.length; j++) v[index[i][j]] = localCoords.fromLocal().times(profv[boundaryPoint[j]]);
        }
        center = new Vec3();
        for (i = 0; i < v.length; i++) center.add(v[i]);
        center.scale(1.0 / v.length);
        for (i = 0; i < v.length; i++) v[i].subtract(center);
        int faces[][] = new int[newFace.size()][];
        for (i = 0; i < faces.length; i++) faces[i] = (int[]) newFace.elementAt(i);
        TriangleMesh mesh = new TriangleMesh(v, faces);
        Edge meshEdge[] = mesh.getEdges();
        for (i = 0; i < newEdge.size(); i++) {
            EdgeInfo info = (EdgeInfo) newEdge.elementAt(i);
            if (info.smoothness == 1.0f) continue;
            for (j = 0; j < meshEdge.length; j++) if ((meshEdge[j].v1 == info.v1 && meshEdge[j].v2 == info.v2) || (meshEdge[j].v1 == info.v2 && meshEdge[j].v2 == info.v1)) meshEdge[j].smoothness = info.smoothness;
        }
        mesh.setSmoothingMethod(Math.max(profile.getSmoothingMethod(), path.getSmoothingMethod()));
        mesh.makeRightSideOut();
        return mesh;
    }
