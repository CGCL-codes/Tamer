    private Matrix4d escalarYCentrar() {
        double centerX = (minX + maxX) / 2;
        double centerY = (minY + maxY) / 2;
        double centerZ = (minZ + maxZ) / 2;
        double scale = 1.0 / Math.max(1.0, getMaxDistance());
        Matrix4d mt = new Matrix4d();
        mt.set(scale, new Vector3d(-centerX * scale, -centerY * scale, -centerZ * scale));
        return mt;
    }
