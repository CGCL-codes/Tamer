    public Query makeLuceneQueryFieldNoBoost(final String fieldName, final BasicQueryFactory qf) {
        return new Query() {

            public String toString(String fn) {
                return getClass().toString() + " " + fieldName + " (" + fn + "?)";
            }

            public Query rewrite(IndexReader reader) throws IOException {
                return getSpanNearQuery(reader, fieldName, getBoost(), qf);
            }
        };
    }
