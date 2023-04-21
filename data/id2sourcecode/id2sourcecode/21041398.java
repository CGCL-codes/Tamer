    public static Transform3D getTransform(double x1, double x2, double y1, double y2, double z1, double z2) {
        Transform3D transform = new Transform3D();
        double cx = (x1 + x2) / 2;
        double cy = (y1 + y2) / 2;
        double cz = (z1 + z2) / 2;
        Vector3d vector = new Vector3d(cx, cy, cz);
        transform.setTranslation(vector);
        double dx = (double) (x2 - x1);
        double dy = (double) (y2 - y1);
        double dz = (double) (z2 - z1);
        float s = (float) Math.sqrt(dx * dx + dy * dy + dz * dz);
        double rotZ = 0;
        if (dx == 0) {
            rotZ = 0;
        } else if (dy == 0) {
            rotZ = Math.PI / 2;
            if (dx < 0) {
                rotZ = -rotZ;
            }
        } else if (dx != 0) {
            rotZ = -Math.atan(Math.abs(dx) / Math.abs(dy));
            if (dy < 0) {
                if (dx > 0) {
                    rotZ = Math.PI + rotZ;
                } else {
                    rotZ = Math.PI * 3 - rotZ;
                }
            } else {
                if (dx > 0) {
                    rotZ = -rotZ;
                }
            }
        }
        rotZ = -rotZ;
        double degrees = rotZ * (180d / Math.PI);
        double rotX = Math.atan(Math.abs(z2 - cz) / (0.5 * Math.sqrt(dx * dx + dy * dy)));
        rotX = -rotX;
        if (z2 - cz > 0) {
            rotX = -rotX;
        }
        degrees = rotX * (180d / Math.PI);
        Transform3D rotationZ = new Transform3D();
        Transform3D rotationX = new Transform3D();
        rotationZ.rotZ(rotZ);
        rotationX.rotX(rotX);
        transform.mul(rotationZ);
        transform.mul(rotationX);
        return transform;
    }
