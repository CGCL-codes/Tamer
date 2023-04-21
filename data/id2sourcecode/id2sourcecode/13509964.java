    public void indexHTMLFiles() throws CorruptIndexException, IOException {
        writer = new IndexWriter(index, new DutchAnalyzer(), true, MaxFieldLength.UNLIMITED);
        reader = IndexReader.open(writer.getDirectory(), true);
        searcher = new IndexSearcher(reader);
        writer.setMergeScheduler(new org.apache.lucene.index.SerialMergeScheduler());
        writer.setRAMBufferSizeMB(320);
        writer.setMergeFactor(10);
        writer.setMaxFieldLength(Integer.MAX_VALUE);
        for (int i = 0; i < list.size(); i++) {
            String path = list.get(i).getPath();
            boolean pass = false;
            if (path.matches("(.)*\\.[a-z]{3,4}$")) {
                pass = true;
                if (path.endsWith("html") || path.endsWith("htm") || path.endsWith("php") || path.endsWith("jps") || path.endsWith("asp")) {
                    pass = false;
                }
            }
            if (pass) continue;
            ParseHTML parser = new ParseHTML(list.get(i));
            if (parser.source == null) continue;
            Document doc = new Document();
            String text = parser.getBodyText();
            doc.add(new Field(FIELD_TEXT, text, Field.Store.YES, Field.Index.ANALYZED));
            String anchor = anchorText(parser);
            doc.add(new Field(FIELD_ANCHOR, anchor, Field.Store.YES, Field.Index.ANALYZED));
            doc.add(new Field(FIELD_ANCHOR_RAW, anchor, Field.Store.YES, Field.Index.NOT_ANALYZED));
            doc.add(new Field(FIELD_URL, parser.source, Field.Store.YES, Field.Index.NOT_ANALYZED));
            System.out.println("Indexing file:" + list.get(i).getPath());
            writer.addDocument(doc);
            writer.commit();
        }
        System.out.println("Optimizing index...");
        writer.optimize();
        writer.close();
    }
