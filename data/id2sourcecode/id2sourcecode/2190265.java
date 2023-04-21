    public void addArchive(URL url) throws ZipException, IOException {
        addArchive(new ZipInputStream(url.openStream()));
    }
