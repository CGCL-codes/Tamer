    public void updateGeometry(Line shape, List<Vector3f> points, int segments, boolean closed, Vector3f up) {
        int np = points.size();
        if (closed) {
            np = np + 3;
        }
        float d[][] = new float[3][np];
        float x[] = new float[np];
        List<Vector3f> path = new ArrayList<Vector3f>();
        for (int i = 0; i < np; i++) {
            Vector3f p;
            if (!closed) {
                p = points.get(i);
            } else {
                if (i == 0) {
                    p = points.get(points.size() - 1);
                } else if (i >= np - 2) {
                    p = points.get(i - np + 2);
                } else {
                    p = points.get(i - 1);
                }
            }
            x[i] = i;
            d[0][i] = p.x;
            d[1][i] = p.y;
            d[2][i] = p.z;
        }
        if (np > 1) {
            float[][] a = new float[3][np];
            float h[] = new float[np];
            for (int i = 1; i <= np - 1; i++) {
                h[i] = x[i] - x[i - 1];
            }
            if (np > 2) {
                float sub[] = new float[np - 1];
                float diag[] = new float[np - 1];
                float sup[] = new float[np - 1];
                for (int i = 1; i <= np - 2; i++) {
                    diag[i] = (h[i] + h[i + 1]) / 3;
                    sup[i] = h[i + 1] / 6;
                    sub[i] = h[i] / 6;
                    for (int dim = 0; dim < 3; dim++) {
                        a[dim][i] = (d[dim][i + 1] - d[dim][i]) / h[i + 1] - (d[dim][i] - d[dim][i - 1]) / h[i];
                    }
                }
                for (int dim = 0; dim < 3; dim++) {
                    solveTridiag(sub.clone(), diag.clone(), sup.clone(), a[dim], np - 2);
                }
            }
            if (!closed) {
                path.add(new Vector3f(d[0][0], d[1][0], d[2][0]));
            }
            float[] point = new float[3];
            for (int i = closed ? 2 : 1; i <= np - 2; i++) {
                for (int j = 1; j <= segments; j++) {
                    for (int dim = 0; dim < 3; dim++) {
                        float t1 = (h[i] * j) / segments;
                        float t2 = h[i] - t1;
                        float v = ((-a[dim][i - 1] / 6 * (t2 + h[i]) * t1 + d[dim][i - 1]) * t2 + (-a[dim][i] / 6 * (t1 + h[i]) * t2 + d[dim][i]) * t1) / h[i];
                        point[dim] = v;
                    }
                    path.add(new Vector3f(point[0], point[1], point[2]));
                }
            }
        }
        this.updateGeometry(shape, path, closed, up);
    }
