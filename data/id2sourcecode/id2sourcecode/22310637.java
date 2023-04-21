    public static Area getAreaForThreshold(double[][] data, double threshold) {
        Vector vec = new Vector(500000);
        System.out.println("getAreaForThreshold(" + threshold + ")");
        Area retarea = null;
        Area tmparea = null;
        Triangle[] triangles = null;
        Point2D.Float[] vert_xys = new Point2D.Float[4];
        double[] vert_values = new double[4];
        for (int a = 0; a < 4; a++) {
            vert_xys[a] = new Point2D.Float();
        }
        GeneralPath tmppath = new GeneralPath();
        for (int y = 0; y < data[0].length - 1; y++) {
            for (int x = 0; x < data.length - 1; x++) {
                vert_values[0] = data[x][y];
                vert_values[0] = data[x + 1][y];
                vert_values[0] = data[x + 1][y + 1];
                vert_values[0] = data[x][y + 1];
                vert_xys[0].x = (float) x;
                vert_xys[0].y = (float) y;
                vert_xys[1].x = (float) (x + 1);
                vert_xys[1].y = (float) y;
                vert_xys[2].x = (float) (x + 1);
                vert_xys[2].y = (float) (y + 1);
                vert_xys[3].x = (float) x;
                vert_xys[3].y = (float) (y + 1);
                triangles = SquareMarcher.marchSquare(vert_xys, vert_values, threshold);
                if (triangles != null) {
                    for (int a = 0; a < triangles.length; a++) vec.add(triangles[a]);
                }
            }
        }
        Triangle[] tris = new Triangle[vec.size()];
        vec.copyInto(tris);
        return getAreaForTriangles(tris);
    }
