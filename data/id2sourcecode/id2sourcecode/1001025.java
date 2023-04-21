    public static WireFrameBox createWireFrameBoxGeometry(GeometryArray[] geometry, BranchGroup boxBG, TransformGroup boxTg, Transform3D t3D, Color3f color) {
        boolean firstLoop = true;
        float minX = 0.0f;
        float maxX = 0.0f;
        float minY = 0.0f;
        float maxY = 0.0f;
        float minZ = 0.0f;
        float maxZ = 0.0f;
        for (int nGeometry = 0; nGeometry < geometry.length; nGeometry++) {
            float[] coords = null;
            int format = geometry[nGeometry].getVertexFormat();
            if ((format & GeometryArray.COORDINATES) > 0) {
                if ((format & GeometryArray.INTERLEAVED) == 0) {
                    coords = new float[geometry[nGeometry].getVertexCount() * 3];
                    geometry[nGeometry].getCoordinates(0, coords);
                } else if ((format & GeometryArray.INTERLEAVED) > 0) {
                    coords = geometry[nGeometry].getInterleavedVertices();
                }
            }
            if (firstLoop) {
                minX = maxX = coords[0];
            }
            for (int nCoords = 0; nCoords < coords.length; nCoords += 3) {
                if (minX > coords[nCoords]) {
                    minX = coords[nCoords];
                }
                if (maxX < coords[nCoords]) {
                    maxX = coords[nCoords];
                }
            }
            if (firstLoop) {
                minY = maxY = coords[1];
            }
            for (int nCoords = 1; nCoords < coords.length; nCoords += 3) {
                if (minY > coords[nCoords]) {
                    minY = coords[nCoords];
                }
                if (maxY < coords[nCoords]) {
                    maxY = coords[nCoords];
                }
            }
            if (firstLoop) {
                minZ = maxZ = coords[2];
            }
            for (int nCoords = 2; nCoords < coords.length; nCoords += 3) {
                if (minZ > coords[nCoords]) {
                    minZ = coords[nCoords];
                }
                if (maxZ < coords[nCoords]) {
                    maxZ = coords[nCoords];
                }
            }
            firstLoop = false;
        }
        float xCenter = (maxX + minX) / 2;
        float yCenter = (maxY + minY) / 2;
        float zCenter = (maxZ + minZ) / 2;
        float xSize = (Math.abs(maxX) + Math.abs(minX)) / 2;
        float ySize = (Math.abs(maxY) + Math.abs(minY)) / 2;
        float zSize = (Math.abs(maxZ) + Math.abs(minZ)) / 2;
        WireFrameBox box = new WireFrameBox(xSize * 1.1f, ySize * 1.1f, zSize * 1.1f, xCenter, yCenter, zCenter, color);
        boxTg.addChild(box);
        boxBG.addChild(boxTg);
        return box;
    }
