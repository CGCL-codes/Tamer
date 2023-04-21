    public void indexDirectory(String path) throws CorruptIndexException, LockObtainFailedException, IOException, ParseException {
        LinkedList<String> files = DirectoryInput.listDirectory(path, "zip");
        writer = new IndexWriter(index, new WhitespaceAnalyzer(), false, MaxFieldLength.UNLIMITED);
        reader = IndexReader.open(writer.getDirectory(), true);
        searcher = new IndexSearcher(reader);
        writer.setMergeScheduler(new org.apache.lucene.index.SerialMergeScheduler());
        writer.setRAMBufferSizeMB(320);
        writer.setMergeFactor(10);
        writer.setMaxFieldLength(Integer.MAX_VALUE);
        WhitespaceAnalyzer analyzer = new WhitespaceAnalyzer();
        q = new QueryParser(FIELD_ID, analyzer);
        for (int i = 0; i < files.size(); i++) {
            String zipPath = files.get(i);
            ZipInput zipFiles = new ZipInput(zipPath);
            LinkedList<InputStream> streams = zipFiles.getZipFilesInputStream();
            System.out.println("Indexing zip file:" + zipPath);
            for (int j = 0; j < streams.size(); j++) {
                WikiPage wiki = AnchorExtractor.parseWikiFile(streams.get(j));
                indexWikiXMLAnchorText(wiki);
            }
            writer.commit();
        }
        writer.optimize();
    }
