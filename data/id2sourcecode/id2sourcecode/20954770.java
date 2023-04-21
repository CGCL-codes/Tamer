    public DocObject inPostDoc(BoardObject board, DocObject doc) throws NetworkException, ContentException {
        String url = HttpConfig.bbsURL() + HttpConfig.BBS_DOC_POST + board.getId();
        if (doc != null) url += "&" + HttpConfig.BBS_CON_FILE_PARAM_NAME + "=" + doc.getId();
        HttpClient client = HttpConfig.newInstance();
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            if (HTTPUtil.isXmlContentType(response)) {
                Document document = XmlOperator.readDocument(entity.getContent());
                return BBSBodyParseHelper.parsePostContent(document, board);
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
