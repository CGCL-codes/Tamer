    public String sendAttFile(File file, BoardObject board, String mimeType) throws NetworkException, ContentException {
        HttpClient client = HttpConfig.newInstance();
        HttpPost post = new HttpPost(HttpConfig.bbsURL() + HttpConfig.BBS_DOC_UPLOAD + board.getName());
        MultipartEntity reqEntity = new MultipartEntity(HttpMultipartMode.STRICT);
        reqEntity.addPart(HttpConfig.BBS_DOC_UPLOAD_FILE_PARAM_NAME, new BBSPostAction().new ExFileBody(file, mimeType == null ? "image/jpeg" : mimeType));
        post.setEntity(reqEntity);
        try {
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (HTTPUtil.isHttp200(response) && HTTPUtil.isXmlContentType(response)) {
                Document doc = XmlOperator.readDocument(entity.getContent());
                return BBSBodyParseHelper.parseAttachURL(doc);
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
