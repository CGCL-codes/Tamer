    public int importData(DocumentCBF doc, String retFile, String[] retAffixs, String[] pictures, String[] affixs, boolean isPublish, String blRtype, boolean blRte) throws Exception {
        saveContent(doc, retFile, retAffixs, blRtype, blRte);
        if (pictures != null && pictures.length > 0) upLoad(doc, pictures, DocResource.PICTURE_TYPE);
        if (affixs != null && affixs.length > 0) upLoad(doc, affixs, DocResource.AFFIX_TYPE);
        if (isPublish) {
            Documents docs = new Documents();
            docs.publish(new int[] { doc.getId() }, new String[] { doc.getChannelPath() }, doc.getCreater(), Function.getSysTime(), Function.getSysTime(), null, true);
            String workItemId = getWorkItemId(doc.getActyInstId());
            docs.endPublishProcess(String.valueOf(doc.getCreater()), Integer.parseInt(doc.getProcessId() == null ? "1" : doc.getProcessId()), workItemId);
        }
        return doc.getId();
    }
