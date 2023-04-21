    public List<String[]> search(File index, String line) throws SearchException {
        try {
            IndexReader reader = IndexReader.open(FSDirectory.open(index), true);
            Searcher searcher = new IndexSearcher(reader);
            Analyzer analyzer = new StandardAnalyzer(Version.LUCENE_CURRENT);
            QueryParser parser = new QueryParser("contents", analyzer);
            Query query = parser.parse(line);
            query = query.rewrite(reader);
            TopDocs hits = searcher.search(query, null, 1000);
            List<String[]> results = new ArrayList<String[]>();
            Highlighter highlighter = new Highlighter(new QueryScorer(query));
            for (int i = 0; i < hits.scoreDocs.length; i++) {
                Document doc = searcher.doc(hits.scoreDocs[i].doc);
                String path = doc.get("path");
                String title = doc.get("title");
                String text = doc.get("contents");
                TokenStream tokenStream = analyzer.tokenStream("contents", new StringReader(text));
                String fragment = highlighter.getBestFragments(tokenStream, text, 3, "...");
                String[] record = new String[3];
                record[0] = path;
                record[1] = title;
                record[2] = fragment;
                results.add(record);
            }
            reader.close();
            return results;
        } catch (Throwable t) {
            m_logger.error("Unable to perform search for " + line + " in " + index.getAbsolutePath().replaceAll("\\\\", "/"), t);
            throw new SearchException(t);
        }
    }
