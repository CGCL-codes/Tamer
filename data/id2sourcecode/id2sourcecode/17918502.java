    public void testWrappedSame() {
        Graph m = Factory.createGraphMem();
        Graph w = new WrappedGraph(m);
        graphAdd(m, "a trumps b; c eats d");
        assertIsomorphic(m, w);
        graphAdd(w, "i write this; you read that");
        assertIsomorphic(w, m);
    }
