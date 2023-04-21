    @Override
    public InputStream openInputStream() throws IOException, SecurityException {
        if (!isGettable()) {
            throw new SecurityException("The platform does not allow to access the input stream!");
        }
        if (is == null) {
            final String locator = getMediaLocator();
            final URL url = new URL(locator);
            return url.openStream();
        } else {
            return is;
        }
    }
