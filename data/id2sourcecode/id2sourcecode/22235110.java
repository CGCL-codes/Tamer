    protected String retriveContent(final String accessUrl) throws Exception {
        final URL url = new URL(accessUrl);
        final BufferedReader inputStream = new BufferedReader(new InputStreamReader(url.openStream()));
        return readWithStringBuffer(inputStream);
    }
