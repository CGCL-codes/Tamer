    public static String getRandomDocId() throws XMLDBException {
        String randomDocId = "";
        Integer objNum = null;
        java.util.Random rand = new java.util.Random();
        int max = 1000;
        for (int i = 0; i < 3; i++) {
            objNum = rand.nextInt(max + 1);
        }
        if (null != objNum) {
            String xquery = "xquery version \"1.0\";" + "  let $col := collection('" + forumDB + "')[" + objNum + "]," + "  $docName := item-at($col, 1)" + "  return replace(util:document-name($docName), \".xml\", \"\")";
            XQueryService service = (XQueryService) vobs.dbaccess.CollectionsManager.getService(rootDB, true, "XQueryService");
            ResourceSet result = service.query(xquery);
            if (result.getSize() > 0) {
                XMLResource resource = (XMLResource) result.getResource(0);
                randomDocId = resource.getContent().toString();
            }
        }
        return randomDocId;
    }
