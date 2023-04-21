    public static OutputStream openUriForwriting(@NotNull final String uri) throws IOException, URISyntaxException {
        final URI theUri = new URI(uri);
        final String scheme = theUri.getScheme();
        if (scheme == null || "file".equals(scheme)) {
            return new FileOutputStream(new File(theUri));
        }
        final URL url = theUri.toURL();
        final URLConnection con = url.openConnection();
        con.setDoInput(true);
        con.setDoOutput(true);
        return con.getOutputStream();
    }
