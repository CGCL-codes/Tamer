    public void downloadList() throws ClientProtocolException, IOException {
        HttpClient httpClient = new DefaultHttpClient();
        HttpGet request = new HttpGet(URL);
        HttpResponse response = httpClient.execute(request);
        HttpEntity entity = response.getEntity();
        XmlPullParser xmlParser;
        try {
            XmlPullParserFactory parserCreator = XmlPullParserFactory.newInstance();
            xmlParser = parserCreator.newPullParser();
            xmlParser.setInput(entity.getContent(), null);
            int parserEvent = xmlParser.getEventType();
            while (parserEvent != XmlPullParser.END_DOCUMENT) {
                if (parserEvent == XmlPullParser.START_TAG) {
                    String tag = xmlParser.getName();
                    if (tag.equals("route")) {
                        String name = xmlParser.getAttributeValue(null, "title");
                        String id = xmlParser.getAttributeValue(null, "id");
                        names.add(name);
                        ids.add(Long.valueOf(id));
                    }
                }
                parserEvent = xmlParser.next();
            }
        } catch (XmlPullParserException e) {
            Log.e(TAG, e.getMessage());
        }
    }
