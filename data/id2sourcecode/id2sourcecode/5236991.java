        private ArrayList<Face> emitPolygon(Obj parent, Vertex[] v) {
            ArrayList<Face> triangles = new ArrayList<Face>();
            int n = v.length;
            int prev, cur, next;
            int[] vp;
            int count;
            int min_vert;
            int i;
            double dist;
            double min_dist;
            boolean poly_orientation;
            boolean beenHere = false;
            vp = new int[n];
            poly_orientation = this.orientation(v);
            for (i = 0; i < n; i++) vp[i] = i;
            count = n;
            while (count > 3) {
                min_dist = Double.MAX_VALUE;
                min_vert = 0;
                for (cur = 0; cur < count; cur++) {
                    prev = cur - 1;
                    next = cur + 1;
                    if (cur == 0) prev = count - 1; else if (next == count) next = 0;
                    if ((determinant(vp[prev], vp[cur], vp[next], v) == poly_orientation) && no_interior(vp[prev], vp[cur], vp[next], v, vp, count, poly_orientation) && ((dist = distance2(v[vp[prev]].getX(), v[vp[prev]].getY(), v[vp[next]].getX(), v[vp[next]].getY())) < min_dist)) {
                        min_dist = dist;
                        min_vert = cur;
                    }
                }
                if (min_dist == Double.MAX_VALUE) {
                    if (beenHere) return null;
                    poly_orientation = !poly_orientation;
                    beenHere = true;
                } else {
                    beenHere = false;
                    prev = min_vert - 1;
                    next = min_vert + 1;
                    if (min_vert == 0) prev = count - 1; else if (next == count) next = 0;
                    triangles.add(new Face(parent, v[vp[prev]], v[vp[min_vert]], v[vp[next]]));
                    count--;
                    for (i = min_vert; i < count; i++) vp[i] = vp[i + 1];
                }
            }
            triangles.add(new Face(parent, v[vp[0]], v[vp[1]], v[vp[2]]));
            return triangles;
        }
