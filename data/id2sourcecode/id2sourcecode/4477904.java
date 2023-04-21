    protected OutputStream outputTo(final Resource target) {
        try {
            final java.io.File file = target.getFile();
            if (file != null) return new FileOutputStream(file);
        } catch (final IOException e) {
        }
        try {
            final URL url = target.getURL();
            if (url != null) {
                final URLConnection connection = url.openConnection();
                connection.setDoOutput(true);
                return connection.getOutputStream();
            }
        } catch (final IOException e) {
        }
        return null;
    }
