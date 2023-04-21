    public void mirrorSelectedVerticesHorizontally() {
        double min = Double.MAX_VALUE, max = -Double.MAX_VALUE;
        double y;
        for (Vertex vertex : selected) {
            y = vertex.getY();
            max = Math.max(max, y);
            min = Math.min(min, y);
        }
        double center = min + (max - min) / 2;
        for (Vertex vertex : selected) {
            y = vertex.getY();
            double offset = (center - y) * 2;
            vertex.setY(y + offset);
        }
        triangulate(true);
    }
