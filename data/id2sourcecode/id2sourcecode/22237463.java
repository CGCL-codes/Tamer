    public Query rewrite(IndexReader reader) throws IOException {
        BooleanQuery result = new BooleanQuery() {

            public Similarity getSimilarity(Searcher searcher) {
                return new DefaultSimilarity() {

                    public float coord(int overlap, int max) {
                        switch(overlap) {
                            case 1:
                                return 1.0f;
                            case 2:
                                return boost;
                            default:
                                return 0.0f;
                        }
                    }
                };
            }
        };
        result.add(match, true, false);
        result.add(context, false, false);
        return result;
    }
