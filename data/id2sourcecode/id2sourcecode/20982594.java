    public RssFeed read(String url) {
        RssFeed rssFeed = new RssFeed();
        try {
            HttpClient httpClient = new DefaultHttpClient();
            httpClient.getParams().setIntParameter("http.connection.timeout", 5000);
            HttpGet getMethod = new HttpGet(url);
            HttpResponse resp = httpClient.execute(getMethod);
            int responseCode = resp.getStatusLine().getStatusCode();
            if (responseCode != 200) {
                return rssFeed;
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            factory.setIgnoringElementContentWhitespace(true);
            factory.setIgnoringComments(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {

                public void warning(SAXParseException e) throws SAXException {
                    logger.warn("caught exception", e);
                    throw e;
                }

                public void error(SAXParseException e) throws SAXException {
                    logger.warn("caught exception", e);
                    throw e;
                }

                public void fatalError(SAXParseException e) throws SAXException {
                    logger.warn("caught exception", e);
                    throw e;
                }
            });
            InputStream in = resp.getEntity().getContent();
            Document doc = builder.parse(in);
            NodeList channels = doc.getElementsByTagName("channel");
            for (int i = 0; i < channels.getLength(); i++) {
                NodeList nodes = channels.item(i).getChildNodes();
                for (int j = 0; j < nodes.getLength(); j++) {
                    Node n = nodes.item(j);
                    if (n.getNodeName().equals("item")) {
                        RssItem rssItem = loadRssItem(n);
                        rssFeed.addItem(rssItem);
                    }
                }
            }
            if (rssFeed.getItems().size() == 0) {
                NodeList items = doc.getElementsByTagName("item");
                for (int i = 0; i < items.getLength(); i++) {
                    RssItem rssItem = loadRssItem(items.item(i));
                    rssFeed.addItem(rssItem);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return rssFeed;
    }
