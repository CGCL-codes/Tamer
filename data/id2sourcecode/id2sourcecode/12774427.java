    public void decompose(DataAdapter data) {
        Region sourceRegion = new Region(0, 0, data.getWidth(), data.getHeight());
        Surface surface = new SurfaceWithSymmetricReflection(new SurfaceAdapter(data, sourceRegion), gap, false, 0);
        printMinMax(surface, 0, data.getWidth());
        List<Surface> decomposedSurfaces = processor.decompose(surface);
        List<List<Surface>> twiceDecomposedSurfaces = new ArrayList<List<Surface>>();
        for (Surface decomposedSurface : decomposedSurfaces) {
            twiceDecomposedSurfaces.add(processor.decompose(decomposedSurface));
        }
        int channelsNumber = processor.getChannelsNumber();
        int horizontalSize = data.getWidth() / channelsNumber;
        int verticalSize = data.getHeight() / channelsNumber;
        for (int cy = 0; cy < channelsNumber; cy++) {
            for (int cx = 0; cx < channelsNumber; cx++) {
                Surface s = twiceDecomposedSurfaces.get(cy).get(cx);
                printMinMax(s, 0, horizontalSize);
                for (int x = 0; x < horizontalSize; x++) {
                    for (int y = 0; y < verticalSize; y++) {
                        int actualX = cx * horizontalSize + x;
                        int actualY = cy * verticalSize + y;
                        if (cy == 1 && cx == 0) {
                            data.setValue(actualX, actualY, s.getValue(x + 1, y) / MULTIPLIER);
                        } else {
                            data.setValue(actualX, actualY, s.getValue(x, y) / MULTIPLIER);
                        }
                    }
                }
            }
        }
    }
