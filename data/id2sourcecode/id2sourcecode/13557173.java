    public List<String> getNeighboursByTag(String string, String indexFieldSearch) {
        Set<String> tags = new HashSet<String>();
        try {
            Analyzer analyzer = new StandardAnalyzer();
            String previous = null;
            String next = null;
            String term = null;
            boolean foundPrevious = false;
            boolean foundNext = false;
            boolean foundTerm = false;
            IndexSearcher isearcher = new IndexSearcher(this.INDEX_DIR);
            QueryParser parser = new QueryParser(indexFieldSearch, analyzer);
            IndexReader reader = IndexReader.open(this.INDEX_DIR);
            Query query = parser.parse(string);
            query = query.rewrite(reader);
            Hits hits = isearcher.search(query);
            Highlighter highlighter = new Highlighter(new QueryScorer(query));
            Fragmenter fragmenter = new SimpleFragmenter(40);
            highlighter.setTextFragmenter(fragmenter);
            for (int i = 0; i < hits.length(); i++) {
                Document doc = hits.doc(i);
                Field field = doc.getField(indexFieldSearch);
                TokenStream tokenStream = analyzer.tokenStream(indexFieldSearch, new StringReader(field.toString()));
                String result = highlighter.getBestFragments(tokenStream, field.toString(), 8, "..");
                StringTokenizer stringTokenizer = new StringTokenizer(result, " ");
                while (stringTokenizer.hasMoreElements()) {
                    String element = ((String) stringTokenizer.nextElement());
                    if (element.equals("<B>" + string + "</B>")) {
                        foundTerm = true;
                        term = element.replace("<B>", "");
                        term = term.replace("</B>", "");
                    } else if (!foundTerm && !foundNext && isValid(element)) {
                        previous = element;
                        foundPrevious = true;
                    } else if (foundTerm && foundPrevious && isValid(element)) {
                        next = element;
                        foundNext = true;
                        if (!previous.trim().equals("")) {
                            tags.add(this.cleanNeibours(previous));
                        }
                        if (!next.trim().equals("")) {
                            tags.add(this.cleanNeibours(next));
                        }
                        foundPrevious = false;
                        foundNext = false;
                        foundTerm = false;
                        break;
                    }
                }
            }
            isearcher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<String>(tags);
    }
