    public List<BlockObject> allBlock() throws NetworkException {
        if (blockList != null) return blockList;
        HttpClient client = HttpConfig.newInstance();
        HttpGet get = new HttpGet(HttpConfig.bbsURL() + HttpConfig.BBS_BOARD);
        blockList = new ArrayList<BlockObject>();
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            Document doc = XmlOperator.readDocument(entity.getContent());
            blockList = BBSBodyParseHelper.parseBlockList(doc);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
        return blockList;
    }
