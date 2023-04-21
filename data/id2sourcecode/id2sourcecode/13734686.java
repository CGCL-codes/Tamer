        public MyPolygon(double[] xpoints, double[] ypoints, int n) {
            x = xpoints;
            y = ypoints;
            npoints = n;
            x_normal = new double[x.length];
            y_normal = new double[y.length];
            for (int i = 0; i < n - 1; i++) {
                x_normal[i] = x[i + 1] - x[i];
                y_normal[i] = y[i + 1] - y[i];
            }
            x_normal[n - 1] = x[0] - x[n - 1];
            y_normal[n - 1] = y[0] - y[n - 1];
        }
