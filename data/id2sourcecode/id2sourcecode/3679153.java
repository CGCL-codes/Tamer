    public URLConnection openConnection() throws IOException {
        assert url != null : uri + " doesn't have the corresponding URL";
        if (proxy != null) return url.openConnection(proxy); else return url.openConnection();
    }
