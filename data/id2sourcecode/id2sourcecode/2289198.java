    private void doRefreshPois() {
        URL url;
        try {
            SharedPreferences prefs;
            prefs = getSharedPreferences(MainPreferences.USER_PREFERENCES, Activity.MODE_PRIVATE);
            String poiFeed = prefs.getString(PoiPreferences.PREF_UPDATE_URL, "http://poi-radar.googlecode.com/files/dolny_slask.kml");
            url = new URL(poiFeed);
            URLConnection connection;
            connection = url.openConnection();
            HttpURLConnection httpConnection = (HttpURLConnection) connection;
            int responseCode = httpConnection.getResponseCode();
            if (responseCode == HttpURLConnection.HTTP_OK) {
                InputStream in = httpConnection.getInputStream();
                DocumentBuilderFactory dbf;
                dbf = DocumentBuilderFactory.newInstance();
                DocumentBuilder db = dbf.newDocumentBuilder();
                Document dom = db.parse(in);
                Element docEle = dom.getDocumentElement();
                NodeList nl = docEle.getElementsByTagName("Placemark");
                if (nl != null && nl.getLength() > 0) {
                    for (int i = 0; i < nl.getLength(); i++) {
                        Element Placemark = (Element) nl.item(i);
                        Element name = (Element) Placemark.getElementsByTagName("name").item(0);
                        Element g = (Element) Placemark.getElementsByTagName("Point").item(0);
                        Element coordinates = (Element) g.getElementsByTagName("coordinates").item(0);
                        String nameString = name.getFirstChild().getNodeValue();
                        String point = coordinates.getFirstChild().getNodeValue();
                        String[] location = point.split(",");
                        Location l = new Location("dummyGPS");
                        l.setLongitude(Double.parseDouble(location[0]));
                        l.setLatitude(Double.parseDouble(location[1]));
                        Poi poi = new Poi(nameString, l);
                        addNewPoi(poi);
                    }
                }
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (ParserConfigurationException e) {
            e.printStackTrace();
        } catch (SAXException e) {
            e.printStackTrace();
        } finally {
        }
    }
