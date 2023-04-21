    public void generateTorus(int n) {
        Point3D A, B, C;
        ArrayList a = new ArrayList();
        double d = 0.3;
        double r = 0.1;
        if (n < 3) n = 3;
        if (n > 100) n = 100;
        int m = (n + 1) / 2;
        if (m < 3) m = 3;
        double aa = 2.0 * Math.PI / n;
        double bb = 2.0 * Math.PI / m;
        for (int i = 0; i < n; i++) {
            double alpha = 2.0 * Math.PI * i / n;
            for (int j = 0; j < m; j++) {
                double beta = 2.0 * Math.PI * j / m;
                A = new Point3D((d + r * Math.cos(beta)) * Math.cos(alpha), (d + r * Math.cos(beta)) * Math.sin(alpha), r * Math.sin(beta));
                B = new Point3D((d + r * Math.cos(beta + bb)) * Math.cos(alpha), (d + r * Math.cos(beta + bb)) * Math.sin(alpha), r * Math.sin(beta + bb));
                C = new Point3D((d + r * Math.cos(beta + bb)) * Math.cos(alpha + aa), (d + r * Math.cos(beta + bb)) * Math.sin(alpha + aa), r * Math.sin(beta + bb));
                a.add(new Triangle3D(A, B, C, 6));
                A = new Point3D((d + r * Math.cos(beta + bb)) * Math.cos(alpha + aa), (d + r * Math.cos(beta + bb)) * Math.sin(alpha + aa), r * Math.sin(beta + bb));
                B = new Point3D((d + r * Math.cos(beta)) * Math.cos(alpha + aa), (d + r * Math.cos(beta)) * Math.sin(alpha + aa), r * Math.sin(beta));
                C = new Point3D((d + r * Math.cos(beta)) * Math.cos(alpha), (d + r * Math.cos(beta)) * Math.sin(alpha), r * Math.sin(beta));
                a.add(new Triangle3D(A, B, C, 8));
            }
        }
        rendering3D.resetRotation();
        rendering3D.setTriangles(a);
        rendering3D.update();
    }
