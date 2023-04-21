    protected InputStream openInput(final String ref) throws FileNotFoundException, DataLoadingException, IOException {
        InputStream input = null;
        if (getType() == InputTypes.XML) {
            input = new BufferedInputStream(new FileInputStream(ref));
            return input;
        }
        if (getType() == InputTypes.HTTP) {
            try {
                final URL url = new URL(ref);
                final HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("GET");
                input = new StringBufferInputStream(VORGURLRequest.getResourceData(conn));
                return input;
            } catch (final MalformedURLException e) {
                throw new DataLoadingException("The input URL for resource is not properly specified.");
            }
        }
        throw new DataLoadingException("The input type is not specified or is not supported.");
    }
