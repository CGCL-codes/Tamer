    protected void decomposeSurface(Surface surface, CompositionType type) {
        int width = surface.getRegion().getWidth();
        int height = surface.getRegion().getHeight();
        List<CoordinateVector> vertices = createDecomposedVertices(width, height);
        List<Surface> decomposedSurfaces = processor.decompose(surface);
        int channelsNumber = processor.getChannelsNumber();
        double[][] result = new double[channelsNumber][];
        int channelDataLength = width * height / channelsNumber;
        for (int i = 0; i < channelsNumber; i++) {
            result[i] = Shaper.pack(decomposedSurfaces.get(i), channelDataLength, vertices);
        }
        if (type == CompositionType.Horizontal) {
            int verticalSize = height / channelsNumber;
            for (int i = 0; i < channelsNumber; i++) {
                for (int k = 0; k < result[i].length; k++) {
                    int x = k % width;
                    int y = k / width + i * verticalSize;
                    surface.setValue(x, y, result[i][k]);
                }
            }
        } else {
            int horizontalSize = width / channelsNumber;
            for (int i = 0; i < channelsNumber; i++) {
                for (int k = 0; k < result[i].length; k++) {
                    int x = k % horizontalSize + i * horizontalSize;
                    int y = k / horizontalSize;
                    surface.setValue(x, y, result[i][k]);
                }
            }
        }
    }
