    private List<DocObject> docContent(DocObject doc, boolean isTitle, DocObject gdoc, String tag) throws NetworkException, ContentException {
        HttpClient client = HttpConfig.newInstance();
        String url = docContentUrl(doc, isTitle, gdoc, tag);
        HttpGet get = new HttpGet(url);
        try {
            HttpResponse response = client.execute(get);
            if (HTTPUtil.isXmlContentType(response)) {
                HttpEntity entity = response.getEntity();
                Document document = XmlOperator.readDocument(entity.getContent());
                return BBSBodyParseHelper.parseContentList(document, doc);
            } else {
                String msg = BBSBodyParseHelper.parseFailMsg(response.getEntity());
                if ("a=a".equals(tag) || "a=n".equals(tag)) msg = msg.replace("往上", "往下");
                throw new ContentException(msg);
            }
        } catch (ContentException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
            throw new NetworkException(e);
        }
    }
