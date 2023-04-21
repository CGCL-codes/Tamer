            public Query rewrite(IndexReader reader) throws IOException {
                return getSpanNearQuery(reader, fieldName, getBoost(), qf);
            }
