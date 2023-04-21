    public static ImageReader getImageReader(ImageWriter writer) {
        if (writer == null) {
            throw new IllegalArgumentException(Messages.getString("imageio.96"));
        }
        ImageWriterSpi writerSpi = writer.getOriginatingProvider();
        if (writerSpi.getImageReaderSpiNames() == null) {
            return null;
        }
        String readerSpiName = writerSpi.getImageReaderSpiNames()[0];
        Iterator<ImageReaderSpi> readerSpis;
        readerSpis = registry.getServiceProviders(ImageReaderSpi.class, true);
        try {
            while (readerSpis.hasNext()) {
                ImageReaderSpi readerSpi = readerSpis.next();
                if (readerSpi.getClass().getName().equals(readerSpiName)) {
                    return readerSpi.createReaderInstance();
                }
            }
        } catch (IOException e) {
        }
        return null;
    }
