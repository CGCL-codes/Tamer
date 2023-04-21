    public RssFeed read() {
        List<RssItem> items = new ArrayList<RssItem>();
        try {
            HttpGet getMethod = new HttpGet(url.toString());
            HttpResponse response = httpClient.execute(getMethod);
            if (response.getStatusLine().getStatusCode() != 200) {
                return null;
            }
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            factory.setValidating(false);
            factory.setNamespaceAware(true);
            factory.setIgnoringElementContentWhitespace(true);
            factory.setIgnoringComments(true);
            DocumentBuilder builder = factory.newDocumentBuilder();
            builder.setErrorHandler(new ErrorHandler() {

                public void warning(SAXParseException e) throws SAXException {
                    System.out.println(e);
                    throw e;
                }

                public void error(SAXParseException e) throws SAXException {
                    System.out.println(e);
                    throw e;
                }

                public void fatalError(SAXParseException e) throws SAXException {
                    System.out.println(e);
                    throw e;
                }
            });
            HttpEntity entity = response.getEntity();
            InputStream in = entity.getContent();
            try {
                Document doc = builder.parse(in);
                NodeList channels = doc.getElementsByTagName("channel");
                for (int i = 0; i < channels.getLength(); i++) {
                    NodeList nodes = channels.item(i).getChildNodes();
                    for (int j = 0; j < nodes.getLength(); j++) {
                        Node n = nodes.item(j);
                        if (n.getNodeName().equals("item")) {
                            RssItem rssItem = loadRssItem(n);
                            items.add(rssItem);
                        }
                    }
                }
            } finally {
                closeQuietly(in);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (httpClient != null) {
                httpClient.getConnectionManager().shutdown();
            }
        }
        return new RssFeed(url, items);
    }
