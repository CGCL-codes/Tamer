    public JSONObject GetJSON() throws Exception {
        HttpClient client = new DefaultHttpClient();
        HttpGet request = new HttpGet();
        double srcLat = src.getLatitudeE6() / 1E6;
        double srcLng = src.getLongitudeE6() / 1E6;
        double destLat = dest.getLatitudeE6() / 1E6;
        double destLng = dest.getLongitudeE6() / 1E6;
        String url = String.format("http://maps.googleapis.com/maps/api/directions/json?origin=%f,%f&destination=%f,%f&sensor=false&mode=walking", srcLat, srcLng, destLat, destLng);
        request.setURI(new URI(url));
        HttpResponse response = client.execute(request);
        BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
        StringBuffer sb = new StringBuffer("");
        String line = "";
        while ((line = in.readLine()) != null) {
            sb.append(line);
        }
        in.close();
        JSONObject json = new JSONObject(sb.toString());
        String status = json.getString("status");
        if (status.equals("OVER_QUERY_LIMIT")) {
            throw new GoogleException("Zuviele Abfragen auf google ausgef�hrt");
        }
        return json;
    }
