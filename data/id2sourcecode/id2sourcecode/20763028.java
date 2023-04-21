    private void DrawPath(GeoPoint src, GeoPoint dest, int color, MapView mapView) {
        StringBuilder urlString = new StringBuilder();
        urlString.append("http://maps.google.com/maps?f=d&hl=en");
        urlString.append("&saddr=");
        urlString.append(Double.toString((double) src.getLatitudeE6() / 1.0E6));
        urlString.append(",");
        urlString.append(Double.toString((double) src.getLongitudeE6() / 1.0E6));
        urlString.append("&daddr=");
        urlString.append(Double.toString((double) dest.getLatitudeE6() / 1.0E6));
        urlString.append(",");
        urlString.append(Double.toString((double) dest.getLongitudeE6() / 1.0E6));
        urlString.append("&ie=UTF8&0&om=0&output=kml");
        Log.d("xxx", "URL=" + urlString.toString());
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
                String path = doc.getElementsByTagName("GeometryCollection").item(0).getFirstChild().getFirstChild().getFirstChild().getNodeValue();
                Log.d("xxx", "path=" + path);
                String[] pairs = path.split(" ");
                String[] lngLat = pairs[0].split(",");
                GeoPoint startGP = new GeoPoint((int) (Double.parseDouble(lngLat[1]) * 1E6), (int) (Double.parseDouble(lngLat[0]) * 1E6));
                mapView.getOverlays().add(new MyOverlay(startGP, startGP, 1));
                GeoPoint gp1;
                GeoPoint gp2 = startGP;
                for (int i = 1; i < pairs.length; i++) {
                    lngLat = pairs[i].split(",");
                    gp1 = gp2;
                    gp2 = new GeoPoint((int) (Double.parseDouble(lngLat[1]) * 1E6), (int) (Double.parseDouble(lngLat[0]) * 1E6));
                    mapView.getOverlays().add(new MyOverlay(gp1, gp2, 2, color));
                    Log.d("xxx", "pair:" + pairs[i]);
                }
                mapView.getOverlays().add(new MyOverlay(dest, dest, 3));
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
