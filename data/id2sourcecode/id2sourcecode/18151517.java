    private Podcast[] grabPodcastXML(String strURL, String station) {
        URL url;
        URLConnection urlConn = null;
        final String TAG = "grabdata - podcast";
        SAXParser saxParser;
        long saxelapsedTimeMillis;
        long saxstart;
        SAXParserFactory factory = SAXParserFactory.newInstance();
        MyContentHandlePodcastSAX myHandler = new MyContentHandlePodcastSAX();
        myHandler.setStation(station);
        try {
            url = new URL(strURL);
            urlConn = url.openConnection();
            Log.d(TAG, "Open connection for data" + strURL);
        } catch (IOException ioe) {
            Log.e(TAG, "Could not connect to " + strURL);
            return null;
        }
        saxstart = System.currentTimeMillis();
        try {
            saxParser = factory.newSAXParser();
            Log.d(TAG, "Before: Parser - SAX");
            InputStream is = urlConn.getInputStream();
            saxParser.parse(is, myHandler);
            is.close();
            Log.d(TAG, "AFTER: Parse - SAX");
        } catch (IOException ioe) {
            Log.e(TAG, "Invalid XML format?? " + ioe.getMessage());
        } catch (ParserConfigurationException pce) {
            Log.e(TAG, "Could not parse XML " + pce.getMessage());
        } catch (SAXException se) {
            Log.e(TAG, "Could not parse XML" + se.getMessage());
        }
        saxelapsedTimeMillis = (System.currentTimeMillis() - saxstart) / 1000;
        Log.d("SAX - TIMER", "Time it took in seconds:" + Long.toString(saxelapsedTimeMillis));
        return myHandler.getPodcasts();
    }
