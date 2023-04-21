    public void getWeightedNeighboursByTag(Tag tagRoot, String indexFieldSearch, PersistenceService persistenceService) {
        List<Tag> tagNeighbours = new ArrayList<Tag>();
        try {
            Analyzer analyzer = new StandardAnalyzer();
            IndexSearcher isearcher = new IndexSearcher(this.INDEX_DIR);
            QueryParser parser = new QueryParser(indexFieldSearch, analyzer);
            IndexReader reader = IndexReader.open(this.INDEX_DIR);
            String string = tagRoot.getTagName().trim().toLowerCase();
            Query query = parser.parse(string);
            query = query.rewrite(reader);
            Hits hits = isearcher.search(query);
            int iLen = 1000;
            if (hits.length() < iLen) {
                iLen = hits.length() - 1;
            }
            for (int i = 0; i < iLen; i++) {
                Document doc = hits.doc(i);
                Field field = doc.getField(indexFieldSearch);
                long idValue = new Long(doc.getField("RESOURCE_ID").stringValue());
                String s = new String(field.stringValue().getBytes(), "UTF-8");
                ArrayList<Tag> neighborsList = this.getTagNeighbors(s, tagRoot, 8, idValue);
                if (neighborsList == null) {
                    System.out.println("test");
                }
                tagNeighbours.addAll(neighborsList);
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
