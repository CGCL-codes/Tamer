    @Override
    protected Vector<PlugInfoCell> doInBackground(String... params) {
        try {
            HttpClient client = new DefaultHttpClient();
            HttpGet get = new HttpGet(params[0]);
            HttpResponse response = client.execute(get);
            HttpEntity entity = response.getEntity();
            long length = entity.getContentLength();
            InputStream is = entity.getContent();
            DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
            DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
            Document doc = docBuilder.parse(is);
            NodeList nlRoot = doc.getElementsByTagName("power");
            return xmlToData(doc);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
