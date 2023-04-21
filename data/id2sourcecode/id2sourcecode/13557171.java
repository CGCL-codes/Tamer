    private void searchAndScore(Analyzer analyzer, File indexDir, String string) throws CorruptIndexException, IOException, ParseException {
        String previous = null;
        String next = null;
        String term = null;
        boolean foundPrevious = false;
        boolean foundNext = false;
        boolean foundTerm = false;
        IndexSearcher isearcher = new IndexSearcher(indexDir.getPath());
        QueryParser parser = new QueryParser(CONTENT, analyzer);
        IndexReader reader = IndexReader.open(indexDir.getPath());
        Query query = parser.parse(string);
        query = query.rewrite(reader);
        Hits hits = isearcher.search(query);
        Highlighter highlighter = new Highlighter(new QueryScorer(query));
        Fragmenter fragmenter = new SimpleFragmenter(40);
        highlighter.setTextFragmenter(fragmenter);
        for (int i = 0; i < hits.length(); i++) {
            Document doc = hits.doc(i);
            System.out.println(doc.get(FILE_NAME));
            Field field = doc.getField(CONTENT);
            TokenStream tokenStream = analyzer.tokenStream(CONTENT, new StringReader(field.toString()));
            String result = highlighter.getBestFragments(tokenStream, field.toString(), 8, "..");
            StringTokenizer stringTokenizer = new StringTokenizer(result, " ");
            while (stringTokenizer.hasMoreElements()) {
                String element = ((String) stringTokenizer.nextElement()).trim().toLowerCase();
                if (element.equals("<B>" + string + "</B>")) {
                    foundTerm = true;
                    term = element.replace("<B>", "");
                    term = term.replace("</B>", "");
                } else if (!foundTerm && !foundNext && (element.length() > 2) && (!element.equals("at")) && (!element.equals("of")) && (!element.equals("off")) && (!element.equals("to")) && (!element.equals("on")) && (!element.equals("in")) && (!element.equals("is"))) {
                    previous = element;
                    foundPrevious = true;
                } else if (foundTerm && foundPrevious && element.length() > 2 && (!element.equals("at")) && (!element.equals("of")) && (!element.equals("off")) && (!element.equals("to")) && (!element.equals("on")) && (!element.equals("in")) && (!element.equals("is"))) {
                    next = element;
                    foundNext = true;
                    System.out.println("Term query  " + term + " has previous neibour      " + previous + "              and next neibour              " + next);
                    foundPrevious = false;
                    foundNext = false;
                    foundTerm = false;
                    break;
                }
            }
        }
        isearcher.close();
    }
