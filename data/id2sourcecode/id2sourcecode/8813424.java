    protected void reconstructSurface(Surface surface, CompositionType type) {
        int width = surface.getRegion().getWidth();
        int height = surface.getRegion().getHeight();
        List<CoordinateVector> vertices = createDecomposedVertices(width, height);
        int channelsNumber = processor.getChannelsNumber();
        int channelDataLength = width * height / channelsNumber;
        double[][] source = new double[channelsNumber][channelDataLength];
        if (type == CompositionType.Horizontal) {
            int verticalSize = height / channelsNumber;
            for (int i = 0; i < channelsNumber; i++) {
                for (int k = 0; k < channelDataLength; k++) {
                    int x = k % width;
                    int y = k / width + i * verticalSize;
                    source[i][k] = surface.getValue(x, y);
                }
            }
        } else {
            int horizontalSize = width / channelsNumber;
            for (int i = 0; i < channelsNumber; i++) {
                for (int k = 0; k < channelDataLength; k++) {
                    int x = k % horizontalSize + i * horizontalSize;
                    int y = k / horizontalSize;
                    source[i][k] = surface.getValue(x, y);
                }
            }
        }
        List<Surface> decomposedSurfaces = new ArrayList<Surface>();
        for (int i = 0; i < channelsNumber; i++) {
            decomposedSurfaces.add(Shaper.unpack(source[i], vertices));
        }
        Surface reconstructedSurface = processor.reconstruct(decomposedSurfaces);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                surface.setValue(x, y, reconstructedSurface.getValue(x, y));
            }
        }
    }
