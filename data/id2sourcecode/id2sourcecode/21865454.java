    public StopWordLoaderImpl() throws IOException {
        ClassLoader loader = StopWordLoaderImpl.class.getClassLoader();
        URL url = loader.getResource(STOP_LIST_FILENAME);
        this.stopWords = IOUtil.readLineSet(url.openStream());
    }
