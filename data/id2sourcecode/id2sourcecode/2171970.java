    public FetchResult query(String query, int pageNo) throws UnsupportedEncodingException, SAXException, IOException {
        if (pageNo <= 0) {
            pageNo = 1;
        }
        List<Book> res = new LinkedList<Book>();
        Node channel = null;
        String xml = fetchXML(query, pageNo);
        Document doc = createDOM(xml);
        channel = getChannelNode(doc);
        NodeList items = channel.getChildNodes();
        res.addAll(parseItems(items));
        FetchResult fetchResult = new FetchResult(res, query);
        parseFetchResult(channel, fetchResult);
        return fetchResult;
    }
