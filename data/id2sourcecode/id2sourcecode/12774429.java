    public void reconstruct(DataAdapter data) {
        int width = data.getWidth();
        int height = data.getHeight();
        int channelsNumber = processor.getChannelsNumber();
        int horizontalSize = width / channelsNumber;
        int verticalSize = height / channelsNumber;
        Region region = new Region(0, 0, horizontalSize, verticalSize);
        Region region2 = new Region(1, 0, horizontalSize, verticalSize);
        List<List<Surface>> twiceDecomposedSurfaces = new ArrayList<List<Surface>>(channelsNumber);
        for (int cy = 0; cy < channelsNumber; cy++) {
            List<Surface> list = new ArrayList<Surface>();
            twiceDecomposedSurfaces.add(list);
            for (int cx = 0; cx < channelsNumber; cx++) {
                int componentIndex = 2 * cy + cx;
                Surface s = new ArraySurface(cy == 1 && cx == 0 ? region2 : region);
                for (int x = 0; x < horizontalSize; x++) {
                    for (int y = 0; y < verticalSize; y++) {
                        int actualX = cx * horizontalSize + x;
                        int actualY = cy * verticalSize + y;
                        if (cy == 1 && cx == 0) {
                            s.setValue(x + 1, y, data.getValue(actualX, actualY) * MULTIPLIER);
                        } else {
                            s.setValue(x, y, data.getValue(actualX, actualY) * MULTIPLIER);
                        }
                    }
                }
                printMinMax(s, 0, horizontalSize);
                list.add(new SurfaceWithSymmetricReflection(s, gap, true, componentIndex));
            }
        }
        List<Surface> decomposedSurfaces = new ArrayList<Surface>(channelsNumber);
        for (int i = 0; i < channelsNumber; i++) {
            decomposedSurfaces.add(processor.reconstruct(twiceDecomposedSurfaces.get(i)));
        }
        Surface reconstructedSurface = processor.reconstruct(decomposedSurfaces);
        printMinMax(reconstructedSurface, 0, width);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                data.setValue(x, y, reconstructedSurface.getValue(x, y));
            }
        }
    }
