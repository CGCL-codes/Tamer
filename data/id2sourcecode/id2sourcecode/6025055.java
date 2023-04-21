    public static OutputStream openOutputStream(final URL url) throws IOException {
        final URLConnection connection = url.openConnection();
        connection.setDoInput(false);
        connection.setDoOutput(true);
        return connection.getOutputStream();
    }
