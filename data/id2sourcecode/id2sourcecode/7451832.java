    public Vector getHotels(String city) {
        mHotels = new Vector();
        HttpGet method;
        String xml;
        HttpResponse resp = null;
        double i = 0;
        try {
            setup();
            client = createHttpClient();
            HttpRequest req = createRequest();
            String url = "http://labos.diee.unica.it/hotel/Availability/xml/list.htm?city=" + city + "&locale=it";
            HttpGet httpget = new HttpGet(url);
            HttpEntity entity = null;
            try {
                HttpResponse rsp = client.execute(httpget);
                entity = rsp.getEntity();
                xml = EntityUtils.toString(entity);
                XmlPullParser parser = Xml.newPullParser();
                parser.setInput(new StringReader(xml));
                int eventType = parser.getEventType();
                while (eventType != XmlPullParser.END_DOCUMENT) {
                    if (eventType == XmlPullParser.START_DOCUMENT) {
                    } else if (eventType == XmlPullParser.END_DOCUMENT) {
                    } else if (eventType == XmlPullParser.START_TAG) {
                        if (parser.getName().equals("structure")) {
                            parser.nextTag();
                            parser.nextText();
                            String id = parser.getText();
                            parser.nextTag();
                            parser.nextTag();
                            parser.nextText();
                            String name = parser.getText();
                            parser.nextTag();
                            parser.nextTag();
                            parser.nextText();
                            String phone = parser.getText();
                            parser.nextTag();
                            parser.nextTag();
                            parser.nextText();
                            double lat = Double.parseDouble(parser.getText());
                            parser.nextTag();
                            parser.nextTag();
                            parser.nextText();
                            double lon = Double.parseDouble(parser.getText());
                            parser.nextTag();
                            parser.nextTag();
                            parser.nextText();
                            String desc = parser.getText();
                            lat = 39.20536000 + (((float) rand.nextInt(1000)) / 50000);
                            lon = 9.13206700 + (((float) rand.nextInt(1000)) / 50000);
                            Hotel hotel = new Hotel(id, name, phone, lat, lon, desc);
                            mHotels.add(hotel);
                        }
                    } else if (eventType == XmlPullParser.END_TAG) {
                    } else if (eventType == XmlPullParser.TEXT) {
                    }
                    eventType = parser.next();
                }
            } finally {
                if (entity != null) entity.consumeContent();
            }
        } catch (Exception e) {
        }
        return mHotels;
    }
