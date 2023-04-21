    public void parseResult() throws Exception {
        URL searchurl = new URL("" + "http://www.ncbi.nlm.nih.gov/blast/Blast.cgi" + "?CMD=Get" + "&FORMAT_TYPE=XML" + "&RID=" + this.rid);
        BlastXMLParserFacade blast = new BlastXMLParserFacade();
        SeqSimilarityAdapter adapter = new SeqSimilarityAdapter();
        blast.setContentHandler(adapter);
        SearchContentHandler builder = new BlastLikeSearchBuilder(results, new DummySequenceDB("queries"), new DummySequenceDBInstallation());
        adapter.setSearchContentHandler(builder);
        blast.parse(new InputSource(searchurl.openStream()));
    }
