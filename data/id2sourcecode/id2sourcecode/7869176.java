    @Override
    protected void updateTarget(float absAnimTime, int baseFrameIndex, int nextFrameIndex, float alpha, ModelAnimation animation) {
        MeshDeformationKeyFrame baseFrame = getFrame(baseFrameIndex);
        MeshDeformationKeyFrame nextFrame = getFrame(nextFrameIndex);
        float[] coords0 = baseFrame.getCoordinates();
        float[] coords1 = nextFrame.getCoordinates();
        Geometry geom = shape.getGeometry();
        int j;
        float[] buffer = new float[3];
        if (baseFrame == nextFrame) {
            geom.setCoordinates(0, coords0);
        } else {
            j = 0;
            for (int i = 0; i < coords0.length; i += 3) {
                buffer[0] = coords0[i + 0] + ((coords1[i + 0] - coords0[i + 0]) * alpha);
                buffer[1] = coords0[i + 1] + ((coords1[i + 1] - coords0[i + 1]) * alpha);
                buffer[2] = coords0[i + 2] + ((coords1[i + 2] - coords0[i + 2]) * alpha);
                geom.setCoordinate(j++, buffer);
            }
        }
        if (baseFrame.getNormals() != null) {
            float[] normals0 = baseFrame.getNormals();
            float[] normals1 = nextFrame.getNormals();
            if (baseFrame == nextFrame) {
                geom.setNormals(0, normals0);
            } else {
                j = 0;
                for (int i = 0; i < normals0.length; i += 3) {
                    buffer[0] = normals0[i + 0] + ((normals1[i + 0] - normals0[i + 0]) * alpha);
                    buffer[1] = normals0[i + 1] + ((normals1[i + 1] - normals0[i + 1]) * alpha);
                    buffer[2] = normals0[i + 2] + ((normals1[i + 2] - normals0[i + 2]) * alpha);
                    geom.setNormal(j++, buffer);
                }
            }
        }
    }
