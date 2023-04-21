    public InputStream getInputStreamForResource(String resourceName) {
        affirm(resourceName != null);
        final URL url = classLoader.findResource(resourceName);
        if (url == null) {
            printMessage(getI18N("enhancer.not_found_resource", resourceName));
            return null;
        }
        final InputStream stream;
        try {
            stream = url.openStream();
        } catch (IOException ex) {
            final String msg = getI18N("enhancer.io_error_while_reading_resource", url.toString(), ex.getMessage());
            throw new RuntimeException(msg);
        }
        affirm(stream != null);
        printMessage(getI18N("enhancer.found_resource", resourceName));
        return stream;
    }
