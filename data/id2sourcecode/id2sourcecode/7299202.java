    public URLConnection openConnection(URL url) throws IOException {
        String spec = url.getFile().trim();
        if (spec.startsWith("/")) spec = spec.substring(1);
        int ix = spec.indexOf("/");
        if (ix == -1) throw new MalformedURLException(NLS.bind(Messages.url_invalidURL, url.toExternalForm()));
        String type = spec.substring(0, ix);
        Constructor construct = (Constructor) connectionType.get(type);
        if (construct == null) throw new MalformedURLException(NLS.bind(Messages.url_badVariant, type));
        PlatformURLConnection connection = null;
        try {
            connection = (PlatformURLConnection) construct.newInstance(new Object[] { url });
        } catch (Exception e) {
            throw new IOException(NLS.bind(Messages.url_createConnection, e.getMessage()));
        }
        connection.setResolvedURL(connection.resolve());
        return connection;
    }
