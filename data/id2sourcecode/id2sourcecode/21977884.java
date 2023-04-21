    public static final byte[] loadByteArray(final URL urlToResource) {
        InputStream in = null;
        try {
            final URLConnection uc = urlToResource.openConnection();
            uc.setUseCaches(false);
            in = uc.getInputStream();
            return CClassLoader.loadByteArray(in);
        } catch (final IOException ioe) {
            return null;
        } finally {
            try {
                in.close();
            } catch (final Exception ignore) {
            }
        }
    }
