    public JSON parse(URL url) throws IOException {
        return parse(url.openConnection().getInputStream());
    }
