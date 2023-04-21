    private Geometry createGeometry(Orbitals orbitals, int index, int parts, double x1, double x2, double y1, double y2, double z1, double z2) {
        double wx, wy, wz, tx, ty, tz;
        double dx = (x2 - x1);
        double dy = (y2 - y1);
        double dz = (z2 - z1);
        long start = System.currentTimeMillis();
        long mem = Runtime.getRuntime().freeMemory();
        double value, length;
        double h = 1E-4;
        Matrix m = new Matrix(3, (parts + 1) * (parts + 1) * (parts + 1));
        int j = 0;
        for (int x = 0; x <= parts; x++) for (int y = 0; y <= parts; y++) for (int z = 0; z <= parts; z++) {
            tx = ((double) x / (double) parts);
            ty = ((double) y / (double) parts);
            tz = ((double) z / (double) parts);
            px[x][y][z] = m.matrix[0][j] = tx * dx + x1;
            py[x][y][z] = m.matrix[1][j] = ty * dy + y1;
            pz[x][y][z] = m.matrix[2][j] = tz * dz + z1;
        }
        start = System.currentTimeMillis() - start;
        System.out.println("0.Zeit:" + start + " ms");
        start = System.currentTimeMillis();
        System.out.println("Memory = " + (mem - Runtime.getRuntime().freeMemory()));
        mem = Runtime.getRuntime().freeMemory();
        start = System.currentTimeMillis() - start;
        System.out.println("1.Zeit:" + start + " ms");
        start = System.currentTimeMillis();
        System.out.println("Memory = " + (mem - Runtime.getRuntime().freeMemory()));
        mem = Runtime.getRuntime().freeMemory();
        j = 0;
        for (int x = 0; x <= parts; x++) for (int y = 0; y <= parts; y++) for (int z = 0; z <= parts; z++) {
            values[x][y][z] = orbitals.getValue(index, px[x][y][z], py[x][y][z], pz[x][y][z]);
            if (Double.isNaN(values[x][y][z])) values[x][y][z] = 0d;
            j++;
        }
        start = System.currentTimeMillis() - start;
        System.out.println("2.Zeit:" + start + " ms");
        start = System.currentTimeMillis();
        System.out.println("Memory = " + (mem - Runtime.getRuntime().freeMemory()));
        mem = Runtime.getRuntime().freeMemory();
        dx /= parts;
        dy /= parts;
        dz /= parts;
        for (int x = 0; x <= parts; x++) for (int y = 0; y <= parts; y++) for (int z = 0; z <= parts; z++) {
            if (x < parts) {
                if (x > 0) gx[x][y][z] = (values[x + 1][y][z] - values[x - 1][y][z]) / (2 * dx); else gx[x][y][z] = (values[x + 1][y][z] - values[x][y][z]) / dx;
            } else gx[x][y][z] = (values[x][y][z] - values[x - 1][y][z]) / dx;
            if (y < parts) {
                if (y > 0) gy[x][y][z] = (values[x][y + 1][z] - values[x][y - 1][z]) / (2 * dy); else gy[x][y][z] = (values[x][y + 1][z] - values[x][y][z]) / dy;
            } else gy[x][y][z] = (values[x][y][z] - values[x][y - 1][z]) / dy;
            if (z < parts) {
                if (z > 0) gz[x][y][z] = (values[x][y][z + 1] - values[x][y][z - 1]) / (2 * dz); else gz[x][y][z] = (values[x][y][z + 1] - values[x][y][z]) / dz;
            } else gz[x][y][z] = (values[x][y][z] - values[x][y][z - 1]) / dz;
            length = Math.sqrt(gx[x][y][z] * gx[x][y][z] + gy[x][y][z] * gy[x][y][z] + gz[x][y][z] * gz[x][y][z]);
            gx[x][y][z] /= length;
            gy[x][y][z] /= length;
            gz[x][y][z] /= length;
        }
        start = System.currentTimeMillis() - start;
        System.out.println("3.Zeit:" + start + " ms");
        start = System.currentTimeMillis();
        System.out.println("Memory = " + (mem - Runtime.getRuntime().freeMemory()));
        mem = Runtime.getRuntime().freeMemory();
        Vector triangles = new Vector();
        GridCell gridCell = new GridCell();
        for (int x = 0; x < parts; x++) for (int y = 0; y < parts; y++) for (int z = 0; z < parts; z++) {
            gridCell.px[0] = px[x][y][z];
            gridCell.px[1] = px[x + 1][y][z];
            gridCell.px[2] = px[x + 1][y][z + 1];
            gridCell.px[3] = px[x][y][z + 1];
            gridCell.px[4] = px[x][y + 1][z];
            gridCell.px[5] = px[x + 1][y + 1][z];
            gridCell.px[6] = px[x + 1][y + 1][z + 1];
            gridCell.px[7] = px[x][y + 1][z + 1];
            gridCell.py[0] = py[x][y][z];
            gridCell.py[1] = py[x + 1][y][z];
            gridCell.py[2] = py[x + 1][y][z + 1];
            gridCell.py[3] = py[x][y][z + 1];
            gridCell.py[4] = py[x][y + 1][z];
            gridCell.py[5] = py[x + 1][y + 1][z];
            gridCell.py[6] = py[x + 1][y + 1][z + 1];
            gridCell.py[7] = py[x][y + 1][z + 1];
            gridCell.pz[0] = pz[x][y][z];
            gridCell.pz[1] = pz[x + 1][y][z];
            gridCell.pz[2] = pz[x + 1][y][z + 1];
            gridCell.pz[3] = pz[x][y][z + 1];
            gridCell.pz[4] = pz[x][y + 1][z];
            gridCell.pz[5] = pz[x + 1][y + 1][z];
            gridCell.pz[6] = pz[x + 1][y + 1][z + 1];
            gridCell.pz[7] = pz[x][y + 1][z + 1];
            gridCell.values[0] = values[x][y][z];
            gridCell.values[1] = values[x + 1][y][z];
            gridCell.values[2] = values[x + 1][y][z + 1];
            gridCell.values[3] = values[x][y][z + 1];
            gridCell.values[4] = values[x][y + 1][z];
            gridCell.values[5] = values[x + 1][y + 1][z];
            gridCell.values[6] = values[x + 1][y + 1][z + 1];
            gridCell.values[7] = values[x][y + 1][z + 1];
            gridCell.gx[0] = gx[x][y][z];
            gridCell.gx[1] = gx[x + 1][y][z];
            gridCell.gx[2] = gx[x + 1][y][z + 1];
            gridCell.gx[3] = gx[x][y][z + 1];
            gridCell.gx[4] = gx[x][y + 1][z];
            gridCell.gx[5] = gx[x + 1][y + 1][z];
            gridCell.gx[6] = gx[x + 1][y + 1][z + 1];
            gridCell.gx[7] = gx[x][y + 1][z + 1];
            gridCell.gy[0] = gy[x][y][z];
            gridCell.gy[1] = gy[x + 1][y][z];
            gridCell.gy[2] = gy[x + 1][y][z + 1];
            gridCell.gy[3] = gy[x][y][z + 1];
            gridCell.gy[4] = gy[x][y + 1][z];
            gridCell.gy[5] = gy[x + 1][y + 1][z];
            gridCell.gy[6] = gy[x + 1][y + 1][z + 1];
            gridCell.gy[7] = gy[x][y + 1][z + 1];
            gridCell.gz[0] = gz[x][y][z];
            gridCell.gz[1] = gz[x + 1][y][z];
            gridCell.gz[2] = gz[x + 1][y][z + 1];
            gridCell.gz[3] = gz[x][y][z + 1];
            gridCell.gz[4] = gz[x][y + 1][z];
            gridCell.gz[5] = gz[x + 1][y + 1][z];
            gridCell.gz[6] = gz[x + 1][y + 1][z + 1];
            gridCell.gz[7] = gz[x][y + 1][z + 1];
            polygonise(gridCell, isolevel, triangles);
            polygonise(gridCell, -isolevel, triangles);
        }
        start = System.currentTimeMillis() - start;
        System.out.println("4.Zeit:" + start + " ms");
        start = System.currentTimeMillis();
        System.out.println("Memory = " + (mem - Runtime.getRuntime().freeMemory()));
        mem = Runtime.getRuntime().freeMemory();
        System.out.println("Count of triangles:" + triangles.size());
        TriangleArray trisArray;
        if (triangles.size() == 0) return null;
        trisArray = new TriangleArray(triangles.size() * 3, TriangleArray.COORDINATES | TriangleArray.NORMALS | TriangleArray.COLOR_3);
        Triangle3D t;
        for (int i = 0; i < triangles.size(); i++) {
            t = (Triangle3D) triangles.elementAt(i);
            trisArray.setCoordinates(i * 3, t.getPoints());
            trisArray.setColors(i * 3, t.getColors());
            trisArray.setNormals(i * 3, t.getNormals());
        }
        start = System.currentTimeMillis() - start;
        System.out.println("5.Zeit:" + start + " ms");
        System.out.println("Memory = " + (mem - Runtime.getRuntime().freeMemory()));
        System.out.println();
        return trisArray;
    }
