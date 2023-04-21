    public static void main(String[] args) throws Exception {
        if (args.length != 0) {
            QUERY = args[0];
        }
        Analyzer analyzer = new PaodingAnalyzer();
        String content = ContentReader.readText(Chinese.class);
        Directory ramDir = new RAMDirectory();
        IndexWriter writer = new IndexWriter(ramDir, analyzer, MaxFieldLength.UNLIMITED);
        Document doc = new Document();
        Field fd = new Field(FIELD_NAME, content, Field.Store.YES, Field.Index.ANALYZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
        doc.add(fd);
        writer.addDocument(doc);
        writer.optimize();
        writer.close();
        IndexReader reader = IndexReader.open(ramDir);
        String queryString = QUERY;
        QueryParser parser = new QueryParser(Version.LUCENE_CURRENT, FIELD_NAME, analyzer);
        Query query = parser.parse(queryString);
        Searcher searcher = new IndexSearcher(ramDir);
        query = query.rewrite(reader);
        System.out.println("Searching for: " + query.toString(FIELD_NAME));
        TopDocs hits = searcher.search(query, 10);
        BoldFormatter formatter = new BoldFormatter();
        Highlighter highlighter = new Highlighter(formatter, new QueryScorer(query));
        highlighter.setTextFragmenter(new SimpleFragmenter(50));
        for (int i = 0; i < hits.scoreDocs.length; i++) {
            int docId = hits.scoreDocs[i].doc;
            Document hit = searcher.doc(docId);
            String text = hit.get(FIELD_NAME);
            int maxNumFragmentsRequired = 5;
            String fragmentSeparator = "...";
            TermPositionVector tpv = (TermPositionVector) reader.getTermFreqVector(docId, FIELD_NAME);
            TokenStream tokenStream = TokenSources.getTokenStream(tpv);
            String result = highlighter.getBestFragments(tokenStream, text, maxNumFragmentsRequired, fragmentSeparator);
            System.out.println("\n" + result);
        }
        reader.close();
    }
