    protected synchronized void load() throws IOException {
        if (url == null) return;
        InputStream in = null;
        try {
            in = url.openStream();
        } catch (Exception urlex) {
            Debug.log(urlex, "[FlexibleProperties.load]: Couldn't find the URL: " + url, module);
        }
        if (in == null) throw new IOException("Could not open resource URL " + url);
        super.load(in);
        in.close();
        if (defaults instanceof FlexibleProperties) ((FlexibleProperties) defaults).reload();
        if (getDoPropertyExpansion()) interpolateProperties();
    }
