    public XMLParser(URL url) throws XmlPullParserException, IOException {
        if (url == null) {
            throw new NullPointerException("url");
        }
        XmlPullParser xpp_ = null;
        InputStream is = null;
        this.source = url.toString();
        try {
            xpp_ = (XmlPullParser) url.getContent(XPP_CLASS);
        } catch (IOException ex) {
        }
        if (xpp_ == null) {
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            factory.setNamespaceAware(false);
            factory.setValidating(false);
            xpp_ = factory.newPullParser();
            is = url.openStream();
            if (is == null) {
                throw new FileNotFoundException(source);
            }
            xpp_.setInput(is, "UTF8");
        }
        this.xpp = xpp_;
        this.inputStream = is;
    }
