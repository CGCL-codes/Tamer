    public EmailResolver(URL resource) throws IOException {
        _urls.load(resource.openStream());
    }
