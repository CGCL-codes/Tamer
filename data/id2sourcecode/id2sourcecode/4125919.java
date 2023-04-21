    @Override
    public JSONArray getRemoteJsonArr(URL url) throws IOException, JSONException {
        URLConnection conn = url.openConnection();
        BufferedReader reader = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF8"));
        response = new StringBuffer();
        String line = "";
        while ((line = reader.readLine()) != null) {
            response.append(line);
        }
        reader.close();
        jSONArray = new JSONArray(response.toString());
        return jSONArray;
    }
