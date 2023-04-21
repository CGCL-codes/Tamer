    private void addTag(Doc doc) {
        String jobname = doc.getValue(tagJobname);
        if (null != jobname) {
            FmsChannel chnl = fmsData.getChannel(jobname);
            if (null != chnl) {
                String name = chnl.getName();
                if (StringUtil.isNotEmpty(name)) {
                    doc.addField(tagChannelName, name);
                }
                String apcColumnId = chnl.getApcColumnId();
                addRegalChannel(doc, apcColumnId);
                String sourceId = chnl.getSourceId();
                if (StringUtil.isNotEmpty(sourceId)) {
                    FmsSource src = fmsData.getSource(sourceId);
                    addTagOfSource(doc, src);
                }
            }
        }
        String publishdate = doc.getValue(tagDate);
        if (null != publishdate) {
            addStatDate(doc, publishdate);
        }
    }
