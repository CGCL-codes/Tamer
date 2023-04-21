    public boolean fwdPostDoc(DocObject doc, String user) throws NetworkException, ContentException {
        HttpClient client = HttpConfig.newInstance();
        HttpPost post = new HttpPost(HttpConfig.bbsURL() + HttpConfig.BBS_DOC_FWD + doc.getBoard().getId());
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair(HttpConfig.BBS_CON_FILE_PARAM_NAME, doc.getId()));
        nvps.add(new BasicNameValuePair(HttpConfig.BBS_DOC_FWD_USER_PARAM_NAME, user));
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, BBSBodyParseHelper.BBS_CHARSET));
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            if (HTTPUtil.isHttp200(response)) {
                String body = HTTPUtil.getHttpBody(entity, BBSBodyParseHelper.BBS_CHARSET);
                if (body.indexOf("文章转寄成功") != -1) return true; else {
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
