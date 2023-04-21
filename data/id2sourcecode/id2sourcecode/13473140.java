    @Override
    public InputStream openStream(URL url) throws IOException {
        try {
            if (exception != null) {
                throw exception;
            }
            return IOUtils.toInputStream(content);
        } finally {
            content = null;
            exception = null;
        }
    }
