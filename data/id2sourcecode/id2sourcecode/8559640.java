    protected String updateBlog() {
        if (updatingBlog) return null;
        updatingBlog = true;
        HttpClient httpclient = null;
        URI uri = null;
        InputStream rssData = null;
        try {
            if (HTTPS) {
                final SchemeRegistry schemeRegistry = new SchemeRegistry();
                schemeRegistry.register(new Scheme("https", SSLSocketFactory.getSocketFactory(), 443));
                final HttpParams params = new BasicHttpParams();
                final SingleClientConnManager mgr = new SingleClientConnManager(params, schemeRegistry);
                httpclient = new DefaultHttpClient(mgr, params);
                uri = new URI(BLOG_URL_HTTPS);
            } else {
                httpclient = new DefaultHttpClient();
                uri = new URI(BLOG_URL);
            }
            final HttpGet request = new HttpGet();
            request.setURI(uri);
            final HttpResponse response = httpclient.execute(request);
            rssData = response.getEntity().getContent();
        } catch (final IOException e) {
            e.printStackTrace();
            updatingBlog = false;
            return "failed";
        } catch (final URISyntaxException e) {
            e.printStackTrace();
            updatingBlog = false;
            return "failed";
        }
        if (rssData == null) {
            updatingBlog = false;
            return "failed";
        }
        final DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
        DocumentBuilder dbdr = null;
        try {
            dbdr = dbf.newDocumentBuilder();
        } catch (final ParserConfigurationException e1) {
            e1.printStackTrace();
            updatingBlog = false;
            return "failed";
        }
        Document doc = null;
        try {
            doc = dbdr.parse(rssData);
        } catch (final SAXException e1) {
            e1.printStackTrace();
            updatingBlog = false;
            return "failed";
        } catch (final IOException e1) {
            e1.printStackTrace();
            updatingBlog = false;
            return "failed";
        }
        final Node rssNode = doc.getElementsByTagName("rss").item(0);
        final NodeList channelNodeList = rssNode.getChildNodes();
        Node channelNode = null;
        for (int i = 0; i < channelNodeList.getLength(); i++) if (channelNodeList.item(i).getNodeName().equalsIgnoreCase("channel")) channelNode = channelNodeList.item(i);
        if (channelNode == null) {
            updatingBlog = false;
            return "failed";
        }
        delete(BLOG_TABLE, null, null);
        String k = "";
        final SimpleDateFormat formatter = new SimpleDateFormat(DATE_FORMAT, Locale.ENGLISH);
        final SimpleDateFormat parser = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH);
        final NodeList itemNodeList = channelNode.getChildNodes();
        for (int i = 0; i < itemNodeList.getLength(); i++) {
            final Node itemNode = itemNodeList.item(i);
            if (itemNode.getNodeType() == Node.ELEMENT_NODE && itemNode.getNodeName().equalsIgnoreCase("item")) {
                final ContentValues item = new ContentValues();
                final Element itemElement = (Element) itemNode;
                for (int j = 1; j < RSS_KEYS.length; j++) {
                    try {
                        final NodeList keyNodeList = itemElement.getElementsByTagName(DeaddropDB.RSS_KEYS[j]);
                        final Element fieldElement = (Element) keyNodeList.item(0);
                        final NodeList value = fieldElement.getChildNodes();
                        k = ((Node) value.item(0)).getNodeValue();
                        if (BLOG_KEYS[j] == KEY_DATE) k = formatter.format(parser.parse(k));
                        item.put(BLOG_KEYS[j], k);
                    } catch (final NullPointerException e) {
                        k = "";
                    } catch (final ParseException e) {
                        Log.v(TAG, "Datum verwerking mislukt! Datum: " + k);
                        e.printStackTrace();
                    }
                }
                for (final String key : BLOG_KEYS) if (!item.containsKey(key) && !key.equals(KEY_ID)) item.put(key, "");
                insert(BLOG_TABLE, item);
            }
        }
        updatingBlog = false;
        return "success";
    }
