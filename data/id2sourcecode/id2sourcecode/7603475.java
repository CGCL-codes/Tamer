    private TriangleMesh triangulateCurve() {
        Vec3 v[] = new Vec3[vertex.length], size = getBounds().getSize();
        Vec2 v2[] = new Vec2[vertex.length];
        int i, j, current, count, min;
        int index[] = new int[vertex.length], faces[][] = new int[vertex.length - 2][3];
        double dir, dir2;
        boolean inside;
        if (size.x > size.y) {
            if (size.y > size.z) j = 2; else j = 1;
        } else {
            if (size.x > size.z) j = 2; else j = 0;
        }
        for (i = 0; i < vertex.length; i++) {
            v[i] = vertex[i].r;
            v2[i] = vertex[i].r.dropAxis(j);
        }
        min = 0;
        for (i = 1; i < v2.length; i++) {
            if (v2[i].x < v2[min].x) min = i;
        }
        for (i = 0; i < index.length; i++) index[i] = i;
        current = min;
        do {
            dir = triangleDirection(v2, index, v2.length, current);
            if (dir == 0.0) {
                current = (current + 1) % index.length;
                if (current == min) return null;
            }
        } while (dir == 0.0);
        count = index.length;
        for (i = 0; i < vertex.length - 2; i++) {
            j = current;
            do {
                dir2 = triangleDirection(v2, index, count, current);
                inside = containsPoints(v2, index, count, current);
                if (dir2 * dir < 0.0 || inside) {
                    current = (current + 1) % count;
                    if (current == j) return null;
                }
            } while (dir2 * dir < 0.0 || inside);
            if (current == 0) faces[i][0] = index[count - 1]; else faces[i][0] = index[current - 1];
            faces[i][1] = index[current];
            if (current == count - 1) faces[i][2] = index[0]; else faces[i][2] = index[current + 1];
            for (j = current; j < count - 1; j++) index[j] = index[j + 1];
            count--;
            current = (current + 1) % count;
        }
        TriangleMesh mesh = new TriangleMesh(v, faces);
        TriangleMesh.Vertex vert[] = (TriangleMesh.Vertex[]) mesh.getVertices();
        return mesh;
    }
