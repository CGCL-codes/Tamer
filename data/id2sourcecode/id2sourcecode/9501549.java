    public static Document buildDocument(String uri) {
        Logger.getLogger("org.lobobrowser").setLevel(Level.WARNING);
        InputStream in = null;
        try {
            UserAgentContext uacontext = new SimpleUserAgentContext();
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            URL url = new URL(uri);
            URLConnection connection = url.openConnection();
            connection.setRequestProperty("User-Agent", "Mozilla/4.0 (compatible; MSIE 6.0;) ");
            in = connection.getInputStream();
            Document document = builder.newDocument();
            String charset = org.lobobrowser.util.Urls.getCharset(connection);
            if (null != charset) {
                if (charset.toUpperCase().indexOf("ISO") >= 0) {
                    charset = "UTF-8";
                }
            }
            Reader reader = new InputStreamReader(in, charset);
            HtmlParser parser = new HtmlParser(uacontext, document);
            parser.parse(reader);
            return document;
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        } finally {
            if (null != in) {
                try {
                    in.close();
                } catch (IOException e) {
                    LOG.error(e.getMessage(), e);
                }
                in = null;
            }
        }
        return null;
    }
