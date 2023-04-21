    public InputStream getResourceAsStream(final Arguments arguments, final String resourceName) {
        Validate.notNull(resourceName, "Resource name cannot be null");
        try {
            final URL url = new URL(resourceName);
            return url.openStream();
        } catch (final Exception e1) {
            return null;
        }
    }
