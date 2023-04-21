    public boolean initIndex(File indexDir) throws CorruptIndexException, LockObtainFailedException, IOException {
        this.indexDir = indexDir;
        boolean exists = false;
        if (indexDir.exists()) exists = true;
        analyzer = new StandardAnalyzer(Version.LUCENE_30);
        Directory directory = FSDirectory.open(indexDir);
        writer = new IndexWriter(directory, analyzer, IndexWriter.MaxFieldLength.LIMITED);
        reader = writer.getReader();
        docDir = new File("Documents");
        return exists;
    }
