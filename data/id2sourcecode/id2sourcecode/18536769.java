    FAFileResource(URI resourceLocator) throws Exception {
        super(resourceLocator);
        URL url = resourceLocator.toURL();
        FAFile = new FAFile(url.openStream());
    }
