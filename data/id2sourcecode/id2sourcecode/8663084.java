    public static void search() throws Exception {
        String indexHome = IndexFiles.INDEX_HOME;
        IndexSearcher searcher = new IndexSearcher(indexHome);
        Date date1 = new Date();
        PaodingAnalyzer panalyzer = new PaodingAnalyzer();
        panalyzer.setKnife(PaodingMaker.make());
        panalyzer.setMode(1);
        QueryParser paser = new QueryParser("body", panalyzer);
        Query query = paser.parse("普元");
        IndexReader reader = searcher.getIndexReader();
        query.rewrite(reader);
        HashSet terms = new HashSet();
        query.extractTerms(terms);
        Iterator it = terms.iterator();
        while (it.hasNext()) {
            Term term = (Term) it.next();
            TermDocs tDocs = reader.termDocs(term);
            while (tDocs.next()) {
                Document tmp = searcher.getIndexReader().document(tDocs.doc());
                System.out.println("\t出现次数：" + tDocs.freq() + "\t符合的文件" + tmp.get("path"));
            }
        }
        Date date2 = new Date();
        System.out.println("search()用时：" + (date2.getTime() - date1.getTime()) + "秒");
    }
