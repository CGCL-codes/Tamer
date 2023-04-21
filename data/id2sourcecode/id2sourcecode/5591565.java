    private String[][] grabdata_popstories(String strURL) {
        URL url;
        URLConnection urlConn = null;
        String TAG = "grabdata - pop stories";
        SAXParser saxParser;
        long saxelapsedTimeMillis;
        long saxstart;
        int byteRead = 0;
        byte[] buf = new byte[1024];
        SAXParserFactory factory = SAXParserFactory.newInstance();
        MyContentHandlePopStoriesSAX myHandler = new MyContentHandlePopStoriesSAX();
        if (updatepopstories == true) {
            Log.d(TAG, "Connect to " + strURL);
            try {
                url = new URL(strURL);
                urlConn = url.openConnection();
                urlConn.setConnectTimeout(10000);
                Log.d(TAG, "Opened connection");
            } catch (IOException ioe) {
                Log.e(TAG, "Could not connect to " + strURL);
            }
            try {
                InputStream is = urlConn.getInputStream();
                FileOutputStream fos = openFileOutput(popfile, Context.MODE_PRIVATE);
                while ((byteRead = is.read(buf)) != -1) {
                    fos.write(buf, 0, byteRead);
                }
                is.close();
                fos.close();
                Log.d(TAG, "Saved xml to file");
                Calendar cal = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT_NOW);
                popstorydate = sdf.format(cal.getTime());
                fos = openFileOutput(popdatefile, 0);
                PrintWriter datewriter = new PrintWriter(fos, true);
                datewriter.println(System.currentTimeMillis());
                fos.close();
                datewriter.close();
                Log.d(TAG, "Saved xml download date to file");
            } catch (FileNotFoundException e) {
                Log.e(TAG, e.toString());
            } catch (IOException ioe) {
                Log.e(TAG, "Unable to save file locally " + ioe.getMessage());
            }
        }
        saxstart = System.currentTimeMillis();
        try {
            FileInputStream fis = openFileInput(popfile);
            saxParser = factory.newSAXParser();
            Log.d(TAG, "Before: Parser - SAX");
            saxParser.parse(fis, myHandler);
            Log.d(TAG, "AFTER: Parse - SAX");
        } catch (FileNotFoundException notfound) {
            Log.e(TAG, "File not found.");
        } catch (IOException ioe) {
            Log.e(TAG, "Invalid XML format?? " + ioe.getMessage());
        } catch (ParserConfigurationException pce) {
            Log.e(TAG, "Could not parse XML " + pce.getMessage());
        } catch (SAXException se) {
            Log.e(TAG, "Could not parse XML " + se.getMessage());
        }
        saxelapsedTimeMillis = (System.currentTimeMillis() - saxstart) / 1000;
        Log.d("SAX - TIMER", "Time it took in seconds:" + Long.toString(saxelapsedTimeMillis));
        String[][] r = { myHandler.getTitles(), myHandler.getImages(), myHandler.getLinks() };
        return r;
    }
