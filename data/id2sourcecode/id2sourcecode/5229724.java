    public IVirtualFile createFile(String name, URL url) {
        try {
            return this.createFile(name, url.openStream());
        } catch (IOException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }
