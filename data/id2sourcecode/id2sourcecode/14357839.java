    @Override
    public void setUp() throws Exception {
        super.setUp();
        directory = new RAMDirectory();
        Analyzer analyzer = new Analyzer() {

            @Override
            public TokenStream tokenStream(String fieldName, Reader reader) {
                return new WhitespaceTokenizer(reader);
            }

            @Override
            public int getPositionIncrementGap(String fieldName) {
                return 100;
            }
        };
        IndexWriter writer = new IndexWriter(directory, analyzer, true, IndexWriter.MaxFieldLength.LIMITED);
        Document doc = new Document();
        doc.add(new Field("field", "one two three four five", Field.Store.YES, Field.Index.ANALYZED));
        doc.add(new Field("repeated", "this is a repeated field - first part", Field.Store.YES, Field.Index.ANALYZED));
        Fieldable repeatedField = new Field("repeated", "second part of a repeated field", Field.Store.YES, Field.Index.ANALYZED);
        doc.add(repeatedField);
        doc.add(new Field("palindrome", "one two three two one", Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc);
        doc = new Document();
        doc.add(new Field("nonexist", "phrase exist notexist exist found", Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc);
        doc = new Document();
        doc.add(new Field("nonexist", "phrase exist notexist exist found", Field.Store.YES, Field.Index.ANALYZED));
        writer.addDocument(doc);
        writer.optimize();
        writer.close();
        searcher = new IndexSearcher(directory, true);
        query = new PhraseQuery();
    }
