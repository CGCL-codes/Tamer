    private boolean parseURL(URL url) {
        boolean parsedOK = false;
        InputStream systemStream = null;
        try {
            systemStream = url.openStream();
            XmlPullParser parser = new MXParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, true);
            parser.setInput(systemStream, "UTF-8");
            int eventType = parser.getEventType();
            do {
                if (eventType == XmlPullParser.START_TAG) {
                    if (parser.getName().equals("host")) {
                        host = parser.nextText();
                    } else if (parser.getName().equals("port")) {
                        port = parseIntProperty(parser, port);
                    } else if (parser.getName().equals("serviceName")) {
                        serviceName = parser.nextText();
                    } else if (parser.getName().equals("chat")) {
                        chatDomain = parser.nextText();
                    } else if (parser.getName().equals("muc")) {
                        mucDomain = parser.nextText();
                    } else if (parser.getName().equals("username")) {
                        usernamnePrefix = parser.nextText();
                    }
                }
                eventType = parser.next();
            } while (eventType != XmlPullParser.END_DOCUMENT);
            parsedOK = true;
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                systemStream.close();
            } catch (Exception e) {
            }
        }
        return parsedOK;
    }
