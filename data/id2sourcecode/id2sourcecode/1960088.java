    public void routeDirections(GeoPoint origen, GeoPoint desti) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.google.com/maps?f=d&hl=en");
        urlString.append("&saddr=");
        urlString.append(Double.toString((double) origen.getLatitudeE6() / 1.0E6));
        urlString.append(",");
        urlString.append(Double.toString((double) origen.getLongitudeE6() / 1.0E6));
        urlString.append("&daddr=");
        urlString.append(Double.toString((double) desti.getLatitudeE6() / 1.0E6));
        urlString.append(",");
        urlString.append(Double.toString((double) desti.getLongitudeE6() / 1.0E6));
        urlString.append("&ie=UTF8&0&om=0&output=kml");
        Log.i(TAG, "URL=" + urlString.toString());
        Document doc = null;
        HttpURLConnection urlConnection = null;
        URL url = null;
        try {
            url = new URL(urlString.toString());
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setRequestMethod("GET");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);
            urlConnection.connect();
            DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
            DocumentBuilder db = dbf.newDocumentBuilder();
            doc = db.parse(urlConnection.getInputStream());
            if (doc.getElementsByTagName("GeometryCollection").getLength() > 0) {
                Location loc = new Location(LocationManager.GPS_PROVIDER);
                locationList.clear();
                String path = doc.getElementsByTagName("GeometryCollection").item(0).getFirstChild().getFirstChild().getFirstChild().getNodeValue();
                Log.i(TAG, "path=" + path);
                String[] pairs = path.split(" ");
                int pairsLength = pairs.length;
                if (pairsLength < 1000) {
                    String[] lngLat = pairs[0].split(",");
                    loc.setLatitude(Double.parseDouble(lngLat[1]));
                    loc.setLongitude(Double.parseDouble(lngLat[0]));
                    locationList.add(loc);
                    for (int i = 1; i < pairsLength; i++) {
                        loc = new Location(LocationManager.GPS_PROVIDER);
                        lngLat = pairs[i].split(",");
                        loc.setLatitude(Double.parseDouble(lngLat[1]));
                        loc.setLongitude(Double.parseDouble(lngLat[0]));
                        locationList.add(loc);
                    }
                    obrirRuta();
                } else {
                    showMessage("Cam� massa llarg per ser mostrat per pantalla");
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
        }
    }
