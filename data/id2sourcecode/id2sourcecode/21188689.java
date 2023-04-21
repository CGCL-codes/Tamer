            @Override
            public Query rewrite(IndexReader reader) {
                return new SpanOrQuery(new SpanQuery[] { new SpanTermQuery(new Term("first", "sally")), new SpanTermQuery(new Term("first", "james")) });
            }
