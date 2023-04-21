    public void blockBoard(BlockObject block) throws NetworkException {
        HttpClient client = HttpConfig.newInstance();
        HttpGet get = new HttpGet(HttpConfig.bbsURL() + HttpConfig.BBS_BLOCK + block.getId());
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            BBSBodyParseHelper.parseBlockBoardList(XmlOperator.readDocument(entity.getContent()), block);
            for (BoardObject board : block.getAllBoardList()) {
                if (board.isDir()) dirBoard(board);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }
