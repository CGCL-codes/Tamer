    public void getNeighboursByTag(Tag tagRoot, String indexFieldSearch, PersistenceService persistenceService) {
        List<Tag> tagNeighbours = new ArrayList<Tag>();
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
            String string = tagRoot.getTagName().trim().toLowerCase();
            Query query = parser.parse(string);
            query = query.rewrite(reader);
            Hits hits = isearcher.search(query);
            Highlighter highlighter = new Highlighter(new QueryScorer(query));
            Fragmenter fragmenter = new SimpleFragmenter(40);
            highlighter.setTextFragmenter(fragmenter);
            for (int i = 0; i < hits.length(); i++) {
                Document doc = hits.doc(i);
                Field field = doc.getField(indexFieldSearch);
                long idValue = new Long(doc.getField("id").stringValue());
                TokenStream tokenStream = analyzer.tokenStream(indexFieldSearch, new StringReader(field.stringValue()));
                String result = highlighter.getBestFragments(tokenStream, field.stringValue(), 8, "..");
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
                        String neighbourName = "";
                        if (!previous.trim().equals("")) {
                            neighbourName = this.cleanNeibours(previous);
                        }
                        if (!next.trim().equals("")) {
                            neighbourName = this.cleanNeibours(next);
                        }
                        Tag newNeighbour = new Tag(neighbourName, tagRoot.getTagAuthor());
                        newNeighbour.setTagRoot(tagRoot);
                        newNeighbour.setResourceId(idValue);
                        newNeighbour.setHasNeighbours(false);
                        tagNeighbours.add(newNeighbour);
                        foundPrevious = false;
                        foundNext = false;
                        foundTerm = false;
                        break;
                    }
                }
            }
            if (tagNeighbours.isEmpty()) {
                tagRoot.setHasNeighbours(false);
            } else {
                tagRoot.setHasNeighbours(true);
                tagRoot.setTagSimblings(tagNeighbours);
            }
            isearcher.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
