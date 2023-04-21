    public int importData(DocumentCBF doc, String retFile, String[] retAffixs, String[] pictures, String[] affixs, boolean isPublish, String blRtype, boolean blRte) throws Exception {
        DocumentCBFDAO docCBFDAO = new DocumentCBFDAO();
        int docId = (int) (IdGenerator.getInstance().getId(IdGenerator.GEN_ID_IP_DOC));
        doc.setId(docId);
        doc.setRemarkProp(1);
        if (doc.getCreateDate() == null) doc.setCreateDate(Function.getSysTime());
        String actyInsyId = create(doc);
        doc.setActyInstId(actyInsyId);
        docCBFDAO.add(doc);
        saveContent(doc, retFile, retAffixs, blRtype, blRte);
        if (pictures != null && pictures.length > 0) upLoad(doc, pictures, DocResource.PICTURE_TYPE);
        if (affixs != null && affixs.length > 0) upLoad(doc, affixs, DocResource.AFFIX_TYPE);
        if (isPublish) {
            Documents docs = new Documents();
            docs.publish(new int[] { doc.getId() }, new String[] { doc.getChannelPath() }, doc.getCreater(), Function.getSysTime(), Function.getSysTime(), null, true);
            String workItemId = getWorkItemId(actyInsyId);
            docs.endPublishProcess(String.valueOf(doc.getCreater()), Integer.parseInt(doc.getProcessId()), workItemId);
        }
        return docId;
    }
