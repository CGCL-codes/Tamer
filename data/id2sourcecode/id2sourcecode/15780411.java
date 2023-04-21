    public static Iterator getImageTranscoders(ImageReader reader, ImageWriter writer) {
        if (reader == null || writer == null) throw new IllegalArgumentException("null argument");
        return getRegistry().getServiceProviders(ImageTranscoderSpi.class, new TranscoderFilter(reader, writer), true);
    }
