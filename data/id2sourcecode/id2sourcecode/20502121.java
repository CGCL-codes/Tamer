    public void dirBoard(BoardObject dirBoard) throws NetworkException {
        HttpClient client = HttpConfig.newInstance();
        HttpGet get = new HttpGet(HttpConfig.bbsURL() + HttpConfig.BBS_BLOCK_DIR + dirBoard.getName());
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            BBSBodyParseHelper.parseChildBoardList(XmlOperator.readDocument(entity.getContent()), dirBoard);
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }
