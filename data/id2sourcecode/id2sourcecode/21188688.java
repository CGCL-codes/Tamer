    public void testRewrite1() throws Exception {
        SpanQuery q = new FieldMaskingSpanQuery(new SpanTermQuery(new Term("last", "sally")) {

            @Override
            public Query rewrite(IndexReader reader) {
                return new SpanOrQuery(new SpanQuery[] { new SpanTermQuery(new Term("first", "sally")), new SpanTermQuery(new Term("first", "james")) });
            }
        }, "first");
        SpanQuery qr = (SpanQuery) searcher.rewrite(q);
        QueryUtils.checkUnequal(q, qr);
        Set<Term> terms = new HashSet<Term>();
        qr.extractTerms(terms);
        assertEquals(2, terms.size());
    }
