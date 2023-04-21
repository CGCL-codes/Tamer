    private void indexFile(int id, Reader reader, int port, IndexPolicyInfo policyInfo, float boost, IndexWriter ramwriter) throws IOException {
        if (reader == null) return;
        Document doc = new Document();
        doc.add(new Field("TIME", String.valueOf(System.currentTimeMillis()), Field.Store.YES, Field.Index.UN_TOKENIZED));
        doc.add(new Field("ID", String.valueOf(id), Field.Store.YES, Field.Index.UN_TOKENIZED));
        doc.add(new Field("content", reader, Field.TermVector.WITH_POSITIONS_OFFSETS));
        doc.add(new Field("PORT", String.valueOf(port), Field.Store.YES, Field.Index.UN_TOKENIZED));
        doc.add(new Field("PID", policyInfo.policyID, Field.Store.YES, Field.Index.UN_TOKENIZED));
        doc.add(new Field("UID", policyInfo.userID, Field.Store.YES, Field.Index.UN_TOKENIZED));
        doc.add(new Field("GID", policyInfo.groupID, Field.Store.YES, Field.Index.UN_TOKENIZED));
        doc.setBoost(boost);
        ramwriter.addDocument(doc);
    }
