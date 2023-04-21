    public List<MailObject> mailList(int start) throws NetworkException, ContentException {
        HttpClient client = HttpConfig.newInstance();
        String url = HttpConfig.bbsURL() + HttpConfig.BBS_MAIL + (start == 0 ? "" : "?" + HttpConfig.BBS_MAIL_START_PARAM_NAME + "=" + start);
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            if (HTTPUtil.isXmlContentType(response)) {
                Document doc = XmlOperator.readDocument(entity.getContent());
                return BBSBodyParseHelper.parseMailList(doc, false);
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
