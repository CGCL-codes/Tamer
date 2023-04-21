    public static ImageWriter getImageWriter(ImageReader reader) {
        if (reader == null) {
            throw new IllegalArgumentException(Messages.getString("imageio.97"));
        }
        ImageReaderSpi readerSpi = reader.getOriginatingProvider();
        if (readerSpi.getImageWriterSpiNames() == null) {
            return null;
        }
        String writerSpiName = readerSpi.getImageWriterSpiNames()[0];
        Iterator<ImageWriterSpi> writerSpis;
        writerSpis = registry.getServiceProviders(ImageWriterSpi.class, true);
        try {
            while (writerSpis.hasNext()) {
                ImageWriterSpi writerSpi = writerSpis.next();
                if (writerSpi.getClass().getName().equals(writerSpiName)) {
                    return writerSpi.createWriterInstance();
                }
            }
        } catch (IOException e) {
        }
        return null;
    }
