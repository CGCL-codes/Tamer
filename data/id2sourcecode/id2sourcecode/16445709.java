    public List<DocObject> topTenDocList() throws NetworkException {
        HttpClient client = HttpConfig.newInstance();
        HttpGet get = new HttpGet(HttpConfig.bbsURL() + HttpConfig.BBS_TOPTEN);
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            Document doc = XmlOperator.readDocument(entity.getContent());
            return BBSBodyParseHelper.parseTopTenList(doc);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }
