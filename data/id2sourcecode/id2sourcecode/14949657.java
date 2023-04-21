    @Test
    public void SimpleOperation() throws IOException {
        makeDirectory();
        IndexWriter writer = new IndexWriter(dir, new SimpleAnalyzer(), true, IndexWriter.MaxFieldLength.UNLIMITED);
        Document doc = new Document();
        doc.add(new Field("partnum", "Q36", Field.Store.YES, Field.Index.NOT_ANALYZED));
        doc.add(new Field("description", buildRandomString(100), Field.Store.NO, Field.Index.ANALYZED));
        writer.addDocument(doc);
        writer.close();
        IndexSearcher searcher = new IndexSearcher(dir);
        Query query = new TermQuery(new Term("partnum", "Q36"));
        TopDocs rs = searcher.search(query, null, 10);
        assertEquals(1, rs.totalHits);
        searcher = new IndexSearcher(dir);
        query = new TermQuery(new Term("partnum", "Q37"));
        rs = searcher.search(query, null, 10);
        assertEquals(0, rs.totalHits);
        printStats("Basic single write single read operations");
    }
