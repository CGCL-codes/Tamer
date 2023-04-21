    public void loadFromURL(String p_url) throws IOException {
        loadFromStream(new URL(p_url).openStream());
    }
