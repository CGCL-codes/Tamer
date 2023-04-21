    public static List<DocObject> findDoc(BoardObject board, String t1, String t2, String t3, String author, int limit, boolean mark, boolean nore) throws NetworkException, ContentException {
        HttpClient client = HttpConfig.newInstance();
        String url = HttpConfig.bbsURL() + HttpConfig.BBS_DOC_FIND + board.getId();
        if (limit <= 0) limit = 1; else if (limit > 30) limit = 30;
        url += "&" + HttpConfig.BBS_DOC_FIND_LIMIT_PARAM_NAME + "=" + limit;
        if (mark) url += HttpConfig.BBS_DOC_FIND_MARK_PARAM_NAME;
        if (nore) url += HttpConfig.BBS_DOC_FIND_NORE_PARAM_NAME;
        try {
            if (author != null) url += "&" + HttpConfig.BBS_DOC_FIND_AUTHOR_PARAM_NAME + "=" + URLEncoder.encode(author, BBSBodyParseHelper.BBS_CHARSET);
            if (t1 != null) url += "&" + HttpConfig.BBS_DOC_FIND_TITLE_PARAM_NAME1 + "=" + URLEncoder.encode(t1, BBSBodyParseHelper.BBS_CHARSET);
            if (t2 != null) url += "&" + HttpConfig.BBS_DOC_FIND_TITLE_PARAM_NAME2 + "=" + URLEncoder.encode(t2, BBSBodyParseHelper.BBS_CHARSET);
            if (t3 != null) url += "&" + HttpConfig.BBS_DOC_FIND_TITLE_PARAM_NAME3 + "=" + URLEncoder.encode(t3, BBSBodyParseHelper.BBS_CHARSET);
            HttpGet get = new HttpGet(url);
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            if (HTTPUtil.isXmlContentType(response)) {
                Document doc = XmlOperator.readDocument(entity.getContent());
                return BBSBodyParseHelper.parseDocList(doc, board);
            } else {
                String msg = BBSBodyParseHelper.parseFailMsg(entity);
                throw new ContentException(msg);
            }
        } catch (ClientProtocolException e) {
            e.printStackTrace();
            throw new NetworkException(e);
        } catch (IOException e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }
