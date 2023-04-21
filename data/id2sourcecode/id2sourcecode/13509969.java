    public void indexAnchorHospitalFiles() throws CorruptIndexException, IOException {
        writer = new IndexWriter(index, new DutchAnalyzer(), true, MaxFieldLength.UNLIMITED);
        readAnchorFromFile();
        readStopAnchor();
        reader = IndexReader.open(writer.getDirectory(), true);
        searcher = new IndexSearcher(reader);
        writer.setMergeScheduler(new org.apache.lucene.index.SerialMergeScheduler());
        writer.setRAMBufferSizeMB(320);
        writer.setMergeFactor(10);
        writer.setMaxFieldLength(Integer.MAX_VALUE);
        Enumeration<String> enu = target_anchor.keys();
        while (enu.hasMoreElements()) {
            String anchor = enu.nextElement();
            String url = target_anchor.get(anchor);
            int freq = anchor_freq.get(anchor);
            if (!isStopAnchor(anchor) && freq > 2 && !isValidAnchor(anchor)) {
                Document doc = new Document();
                doc.add(new Field(FIELD_TEXT, anchor, Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field(FIELD_ANCHOR, anchor, Field.Store.YES, Field.Index.ANALYZED));
                doc.add(new Field(FIELD_ANCHOR_RAW, anchor, Field.Store.YES, Field.Index.NOT_ANALYZED));
                doc.add(new Field(FIELD_URL, url, Field.Store.YES, Field.Index.NOT_ANALYZED));
                System.out.println("Indexing file:" + anchor);
                writer.addDocument(doc);
                writer.commit();
            }
        }
        System.out.println("Optimizing index...");
        writer.optimize();
        writer.close();
    }
