    private long putPLace(int i) throws IOException {
        long startTime = System.currentTimeMillis();
        URL url = new URL(URL_REST);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("PUT");
        conn.setRequestProperty("Content-type", "application/json");
        OutputStreamWriter out = new OutputStreamWriter(conn.getOutputStream());
        String lng = Double.toString(PlaceGenerateUtils.randomDouble(31.9407876f, 32.1612008f));
        String lat = Double.toString(PlaceGenerateUtils.randomDouble(49.3694599f, 49.4657723f));
        String content = "{" + "\"name\": \"nameABC_" + i + "\"" + ", " + "\"mlng\": " + lng + ", \"mlat\": " + lat + ", " + "\"lng\": " + lng + ", \"lat\": " + lat + ", " + "\"zoom\": 15, " + "\"mapProvider\": \"google\", " + "\"mapType\": \"sat\", " + "\"mapW\": 854, " + "\"mapH\": 480, " + "\"createdBy\": \"createdBy\" " + "}";
        out.write(content);
        out.close();
        conn.getInputStream();
        long time = System.currentTimeMillis() - startTime;
        return time;
    }
