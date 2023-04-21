    private BufferedReader createReader(final String dataFile) throws IOException {
        final URL url = new URL(getCodeBase(), dataFile);
        final URLConnection connection = url.openConnection();
        final BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()));
        return reader;
    }
