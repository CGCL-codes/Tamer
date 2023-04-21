    public static String getContent(String name) throws IOException {
        final URL url = getURL(name);
        Reader reader = new InputStreamReader(url.openStream());
        StringWriter writer = new StringWriter();
        try {
            IOUtils.copy(reader, writer);
        } finally {
            IOUtils.closeQuietly(reader);
            IOUtils.closeQuietly(writer);
        }
        return writer.toString();
    }
