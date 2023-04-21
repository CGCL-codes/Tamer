    Geometry createGeometry() {
        double center_x = meansize + (rand.nextDouble() * (xrange - (2 * meansize)));
        double center_y = meansize + (rand.nextDouble() * (yrange - (2 * meansize)));
        double size = rand.nextDouble() * 2 * meansize;
        int n_vertices = 3 + rand.nextInt(max_vertices - 3);
        double[] angles = new double[n_vertices];
        double[] distances = new double[n_vertices];
        for (int k = 0; k < n_vertices; k++) {
            angles[k] = rand.nextDouble() * 2 * Math.PI;
            distances[k] = rand.nextDouble() * size;
        }
        sort(angles);
        Coordinate[] coords = new Coordinate[n_vertices + 1];
        for (int k = 0; k < n_vertices; k++) {
            double x = center_x + (distances[k] * Math.cos(angles[k]));
            double y = center_y + (distances[k] * Math.sin(angles[k]));
            coords[k] = new Coordinate(x, y);
        }
        coords[n_vertices] = coords[0];
        CoordinateSequence cs = new CoordinateArraySequence(coords);
        return new LineString(cs, gfact);
    }
