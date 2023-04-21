    public static final byte[] loadByteArray(final URL urlToResource) {
        InputStream in = null;
        try {
            URLConnection uc = urlToResource.openConnection();
            uc.setUseCaches(false);
            in = uc.getInputStream();
            return loadByteArray(in);
        } catch (final IOException ioe) {
            return null;
        } finally {
            try {
                in.close();
            } catch (final Exception ignore) {
            }
        }
    }
