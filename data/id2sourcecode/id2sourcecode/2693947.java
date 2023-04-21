    public long syncDB(Handler h, long id, String rssurl) throws Exception {
        mID = id;
        mRSSURL = rssurl;
        SAXParserFactory spf = SAXParserFactory.newInstance();
        SAXParser sp = spf.newSAXParser();
        XMLReader xr = sp.getXMLReader();
        xr.setContentHandler(this);
        xr.setErrorHandler(this);
        URL url = new URL(mRSSURL);
        URLConnection c = url.openConnection();
        c.setRequestProperty("User-Agent", "Android/m3-rc37a");
        xr.parse(new InputSource(c.getInputStream()));
        return mID;
    }
