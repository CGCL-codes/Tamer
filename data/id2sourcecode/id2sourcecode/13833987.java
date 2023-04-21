    public static void main(String[] args) {
        Directory ramDir = new RAMDirectory();
        try {
            IndexWriter writer = new IndexWriter(ramDir, XFactory.getWriterAnalyzer());
            Document doc = new Document();
            Field fd = new Field(FIELD_NAME, CONTENT, Field.Store.YES, Field.Index.TOKENIZED, Field.TermVector.WITH_POSITIONS_OFFSETS);
            doc.add(fd);
            writer.addDocument(doc);
            writer.optimize();
            writer.close();
            IndexReader reader = IndexReader.open(ramDir);
            String queryString = QUERY;
            QueryParser parser = new QueryParser(FIELD_NAME, XFactory.getWriterAnalyzer());
            Query query = parser.parse(queryString);
            System.out.println(query);
            Searcher searcher = new IndexSearcher(ramDir);
            query = query.rewrite(reader);
            System.out.println(query);
            System.out.println("Searching for: " + query.toString(FIELD_NAME));
            Hits hits = searcher.search(query);
            BoldFormatter formatter = new BoldFormatter();
            Highlighter highlighter = new Highlighter(formatter, new QueryScorer(query));
            highlighter.setTextFragmenter(new SimpleFragmenter(50));
            for (int i = 0; i < hits.length(); i++) {
                String text = hits.doc(i).get(FIELD_NAME);
                int maxNumFragmentsRequired = 5;
                String fragmentSeparator = "...";
                TermPositionVector tpv = (TermPositionVector) reader.getTermFreqVector(hits.id(i), FIELD_NAME);
                TokenStream tokenStream = TokenSources.getTokenStream(tpv);
                String result = highlighter.getBestFragments(tokenStream, text, maxNumFragmentsRequired, fragmentSeparator);
                System.out.println("\n" + result);
            }
            reader.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
