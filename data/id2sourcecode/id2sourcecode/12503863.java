    private ByteArrayOutputStream availableRead(InputStream is) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            while (is.available() > 0) {
                baos.write(is.read());
            }
        } catch (IOException ex) {
            throw new CascadingRuntimeException(ex);
        }
        return baos;
    }
