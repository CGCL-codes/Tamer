    public static String getGcAuthUrl() {
        String result = "";
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(CB_API_URL_GET_URLS);
            httppost.setHeader("Accept", "application/json");
            httppost.setHeader("Content-type", "application/json");
            HttpResponse response = httpclient.execute(httppost);
            BufferedReader rd = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            String line = "";
            while ((line = rd.readLine()) != null) {
                result += line + "\n";
            }
            try {
                JSONTokener tokener = new JSONTokener(result);
                JSONObject json = (JSONObject) tokener.nextValue();
                return json.getString("GcAuth_ACB");
            } catch (JSONException e) {
                e.printStackTrace();
            }
        } catch (Exception ex) {
            System.out.println(ex.getMessage());
            return "";
        }
        return "";
    }
