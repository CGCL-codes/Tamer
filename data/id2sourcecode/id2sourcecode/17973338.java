    public static Descriptor buildDescriptor(final URL url) {
        try {
            return buildDescriptor(url.openStream());
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
