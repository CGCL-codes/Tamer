    public void lookup(String ifid) throws MalformedIFIDException, IOException {
        URL url;
        try {
            url = urlOfIfid(ifid);
        } catch (MalformedURLException e) {
            throw new MalformedIFIDException(ifid);
        }
        SAXParser sp;
        XMLReader xr;
        try {
            sp = factory.newSAXParser();
            xr = sp.getXMLReader();
        } catch (Exception e) {
            throw new RuntimeException("Unexpected failure while creating SAX parser", e);
        }
        IFictionHandler handler = new IFictionHandler();
        xr.setContentHandler(handler);
        try {
            xr.parse(new InputSource(url.openStream()));
        } catch (IOException e) {
            Log.w(TAG, "IO exception while fetching record on " + ifid + " from IFDb, possibly doesn't exist", e);
            return;
        } catch (SAXException e) {
            Log.e(TAG, "SAX exception while parsing record on " + ifid, e);
        }
        final String coverArt = handler.getCoverArt();
        if (coverArt != null) try {
            fetchCover(ifid, coverArt);
        } catch (IOException e) {
            Log.e(TAG, "IO error when fetching cover for " + ifid, e);
        }
        ContentValues values = handler.getValues();
        values.put(Games.LOOKED_UP, true);
        mContentResolver.update(Games.uriOfIfid(ifid), values, null, null);
    }
