    public List<DocObject> boardDoc(BoardObject board, boolean isTitle, int fetchedTotalCount, int stickyCount) throws NetworkException {
        int start = 0;
        String url;
        if (fetchedTotalCount == 0) {
            url = boardDocUrl(board, isTitle, 0);
        } else {
            start = calculateStartFromFetched(board, fetchedTotalCount, stickyCount);
            url = boardDocUrl(board, isTitle, start < 1 ? 1 : start);
        }
        HttpClient client = HttpConfig.newInstance();
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            Document doc = XmlOperator.readDocument(entity.getContent());
            if (fetchedTotalCount == 0) return BBSBodyParseHelper.parseDocList(doc, board); else return dealFirstPage(BBSBodyParseHelper.parseDocList(doc, board), start < 1 ? -start + 1 : 0);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }
