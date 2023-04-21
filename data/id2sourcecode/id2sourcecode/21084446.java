    private static void processDocument(XmlPullParser xpp, URL url) throws XmlPullParserException, IOException {
        InputStream in = null;
        try {
            in = url.openStream();
            xpp.setInput(in, CHARACTER_ENCODING);
            int eventType = xpp.getEventType();
            do {
                if (eventType == XmlPullParser.START_DOCUMENT) {
                } else if (eventType == XmlPullParser.END_DOCUMENT) {
                } else if (eventType == XmlPullParser.START_TAG) {
                    try {
                        startElement(xpp.getName(), url);
                    } catch (Exception e) {
                    }
                } else if (eventType == XmlPullParser.END_TAG) {
                    try {
                        endElement(xpp.getName(), content);
                    } catch (Exception e) {
                    }
                } else if (eventType == XmlPullParser.TEXT) {
                    content = xpp.getText().trim();
                } else {
                }
                try {
                    eventType = xpp.next();
                } catch (java.io.IOException ex) {
                    return;
                } catch (XmlPullParserException e) {
                    return;
                }
            } while (eventType != XmlPullParser.END_DOCUMENT);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException _ignore) {
                }
            }
        }
    }
