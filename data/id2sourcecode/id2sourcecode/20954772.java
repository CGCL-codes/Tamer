    public boolean sendPostDoc(DocObject newdoc, DocObject olddoc, boolean anony, boolean edit, int sig) throws NetworkException, ContentException {
        HttpClient client = HttpConfig.newInstance();
        HttpPost post = new HttpPost(HttpConfig.bbsURL() + HttpConfig.BBS_DOC_SEND + (newdoc != null ? newdoc.getBoard().getId() : olddoc.getBoard().getId()));
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        if (olddoc != null) nvps.add(new BasicNameValuePair(HttpConfig.BBS_CON_FILE_PARAM_NAME, olddoc.getId()));
        if (newdoc != null) nvps.add(new BasicNameValuePair(HttpConfig.BBS_DOC_POST_BOARD_PARAM_NAME, newdoc.getBoard().getName())); else if (olddoc != null) nvps.add(new BasicNameValuePair(HttpConfig.BBS_DOC_POST_BOARD_PARAM_NAME, olddoc.getBoard().getName()));
        nvps.add(new BasicNameValuePair(HttpConfig.BBS_DOC_POST_EDIT_PARAM_NAME, edit ? "1" : "0"));
        nvps.add(new BasicNameValuePair(HttpConfig.BBS_DOC_POST_ANONY_PARAM_NAME, anony ? "1" : "0"));
        nvps.add(new BasicNameValuePair(HttpConfig.BBS_DOC_POST_SIG_PARAM_NAME, String.valueOf(sig)));
        nvps.add(new BasicNameValuePair(HttpConfig.BBS_DOC_POST_TITLE_PARAM_NAME, newdoc != null ? newdoc.getTitle() : olddoc.getTitle()));
        nvps.add(new BasicNameValuePair(HttpConfig.BBS_DOC_POST_CONTENT_PARAM_NAME, newdoc != null ? newdoc.getContent() : olddoc.getContent()));
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, BBSBodyParseHelper.BBS_CHARSET));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (HTTPUtil.isHttp200(response)) {
                String body = HTTPUtil.getHttpBody(entity, BBSBodyParseHelper.BBS_CHARSET);
                if (body.indexOf("成功") != -1) return true; else {
                    String msg = BBSBodyParseHelper.parseFailMsg(body);
                    throw new ContentException(msg);
                }
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
