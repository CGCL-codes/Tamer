    public IVirtualFile createFile(IVirtualPath path, URL url) {
        try {
            return this.createFile(path, url.openStream());
        } catch (IOException e) {
            throw ThrowableManagerRegistry.caught(e);
        }
    }
