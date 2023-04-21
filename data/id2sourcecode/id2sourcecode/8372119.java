    public Include(URL url) throws IOException {
        if (url != null) reader = new InputStreamReader(url.openStream());
    }
