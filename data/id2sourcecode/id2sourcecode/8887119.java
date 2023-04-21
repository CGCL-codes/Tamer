    public Resource createResource(URL url) throws IOException {
        return new Resource(url.toString(), getType(), newReader(url.openStream()));
    }
