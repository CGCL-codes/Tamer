    public Point3f getCenter() {
        float x = (minX + maxX) / 2;
        float y = (minY + maxY) / 2;
        float z = (minZ + maxZ) / 2;
        return new Point3f(x, y, z);
    }
