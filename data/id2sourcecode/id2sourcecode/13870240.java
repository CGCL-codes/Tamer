    protected void innerProcess(CrawlURI crawlURI) {
        Ugc ugc = (Ugc) crawlURI.getModel();
        String playUrl = ugc.getPlayUrl();
        String videoSite = ugc.getVideoSite();
        String vid = ugc.getVid();
        String evid = ugc.getEvid();
        int length = ugc.getLength();
        if (playUrl.endsWith("/")) {
            playUrl = playUrl.substring(0, playUrl.length() - 1);
        }
        CriteriaInfo ci = new CriteriaInfo();
        ci.eq("playUrl", playUrl);
        List<Ugc> ugcList = null;
        if (!canProcess(crawlURI)) {
            if (isInvalidPage(crawlURI)) {
            }
            return;
        }
        try {
            IDocument doc = DocumentFactory.newDocument(ugc.getPlayUrl() + "");
            try {
                doc.addElement(DocumentFactory.newString("vid", ugc.getId() + ""));
                doc.addElement(DocumentFactory.newString("url", ugc.getPlayUrl() + ""));
                doc.addElement(DocumentFactory.newString("vvid", ugc.getVid()));
                doc.addElement(DocumentFactory.newString("vevid", ugc.getEvid()));
                doc.addElement(DocumentFactory.newString("vtitle", ugc.getTitle()));
                doc.addElement(DocumentFactory.newString("vlogo", ugc.getLogo()));
                doc.addElement(DocumentFactory.newString("vplayurl", ugc.getPlayUrl()));
                doc.addElement(DocumentFactory.newInteger("vlength", ugc.getLength()));
                doc.addElement(DocumentFactory.newString("vchannel", ugc.getChannel()));
                doc.addElement(DocumentFactory.newString("vcategorys", ugc.getCategory()));
                doc.addElement(DocumentFactory.newString("vtags", ugc.getTags()));
                doc.addElement(DocumentFactory.newString("vsourcesite", ugc.getSourceSite()));
                doc.addElement(DocumentFactory.newString("vuploaduserid", ugc.getUploadUserid()));
                doc.addElement(DocumentFactory.newString("vuploadusername", ugc.getUploadUsername()));
                doc.addElement(DocumentFactory.newDate("uuseeupdatedate", ugc.getUpdateDate()));
            } catch (DuplicateElementException e) {
            }
            long operationId = feeder.addDocument(doc);
            feeder.waitForCompletion();
            IDocumentFeederStatus status = feeder.getStatusReport();
            printStatus(status);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
