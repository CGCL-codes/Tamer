    public static InputStream cycleGetResource(final String resourceName) {
        final URL url = cycleGetUrl(resourceName);
        if (url != null) {
            try {
                return url.openStream();
            } catch (final IOException e) {
                e.printStackTrace();
            }
        }
        return null;
    }
