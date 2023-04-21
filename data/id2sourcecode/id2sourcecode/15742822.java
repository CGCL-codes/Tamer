    public IndexSingle(File configDir, IndexConfig indexConfig, boolean createIfNotExists) throws IOException, URISyntaxException, InstantiationException, IllegalAccessException, ClassNotFoundException, SearchLibException {
        super(indexConfig);
        online = true;
        readonly = false;
        if (indexConfig.getNativeOSSE() || true == Boolean.TRUE) {
            reader = new ReaderNativeOSSE(configDir, indexConfig);
            writer = new WriterNativeOSSE(configDir, indexConfig, (ReaderNativeOSSE) reader);
        } else {
            reader = ReaderLocal.fromConfig(configDir, indexConfig, createIfNotExists);
            writer = new WriterLocal(indexConfig, (ReaderLocal) reader);
        }
    }
