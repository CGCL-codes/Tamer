    private Geometry binaryUnion(List geoms, int start, int end) {
        if (end - start <= 1) {
            Geometry g0 = getGeometry(geoms, start);
            return unionSafe(g0, null);
        } else if (end - start == 2) {
            return unionSafe(getGeometry(geoms, start), getGeometry(geoms, start + 1));
        } else {
            int mid = (end + start) / 2;
            Geometry g0 = binaryUnion(geoms, start, mid);
            Geometry g1 = binaryUnion(geoms, mid, end);
            return unionSafe(g0, g1);
        }
    }
