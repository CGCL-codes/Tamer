    private static List<RGBImage> decomposeImage(RGBImage image, Processor processor) {
        List<List<Surface>> decomposedSurfaces = new ArrayList<List<Surface>>();
        for (Surface surface : image.getSurfaces()) {
            decomposedSurfaces.add(processor.decompose(surface));
        }
        List<RGBImage> result = new ArrayList<RGBImage>();
        for (int i = 0; i < processor.getChannelsNumber(); i++) {
            result.add(new RGBImage(decomposedSurfaces.get(0).get(i), decomposedSurfaces.get(1).get(i), decomposedSurfaces.get(2).get(i)));
        }
        return result;
    }
