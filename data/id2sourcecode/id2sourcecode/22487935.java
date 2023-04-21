    private static DecomposedImage decomposeStep(RGBImage source, Processor processor) {
        int channelsNumber = processor.getChannelsNumber();
        List<List<Surface>> decomposedSurfaces = new ArrayList<List<Surface>>();
        for (Surface surface : source.getSurfaces()) {
            decomposedSurfaces.add(processor.decompose(surface));
        }
        int imageComponentsNumber = source.getSurfaces().size();
        DecomposedImage result = new DecomposedImage();
        Surface[] mainImageSurfaces = new Surface[imageComponentsNumber];
        for (int i = 0; i < imageComponentsNumber; i++) {
            mainImageSurfaces[i] = decomposedSurfaces.get(i).get(0);
        }
        result.setMainImage(new RGBImage(mainImageSurfaces[0], mainImageSurfaces[1], mainImageSurfaces[2]));
        for (int c = 1; c < channelsNumber; c++) {
            Surface[] imageSurfaces = new Surface[imageComponentsNumber];
            for (int i = 0; i < imageComponentsNumber; i++) {
                imageSurfaces[i] = decomposedSurfaces.get(i).get(c);
            }
            result.addImage(new RGBImage(imageSurfaces[0], imageSurfaces[1], imageSurfaces[2]));
        }
        return result;
    }
