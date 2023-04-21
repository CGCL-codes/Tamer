    public void mirrorSelectedVerticesVertically() {
        double min = Double.MAX_VALUE, max = -Double.MAX_VALUE;
        double x;
        for (Vertex vertex : selected) {
            x = vertex.getX();
            max = Math.max(max, x);
            min = Math.min(min, x);
        }
        double center = min + (max - min) / 2;
        for (Vertex vertex : selected) {
            x = vertex.getX();
            double offset = (center - x) * 2;
            vertex.setX(x + offset);
        }
        triangulate(true);
    }
