    public static String[] getGetFriendsList(PossessedApplication app) {
        ArrayList<String> rtnarr = new ArrayList<String>();
        try {
            String queryparms = "access_token=" + URLEncoder.encode(app.getAuthCode(), "UTF-8");
            URL url = new URL("https://graph.facebook.com/me/friends?" + queryparms);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JSONParser p = new JSONParser();
            JSONObject ob = (JSONObject) p.parse(rd);
            JSONArray rtn = (JSONArray) ob.get("data");
            Iterator<JSONObject> elm = rtn.iterator();
            while (elm.hasNext()) {
                JSONObject jsonObject = (JSONObject) elm.next();
                rtnarr.add(jsonObject.get("id").toString());
            }
            rd.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return rtnarr.toArray(new String[] {});
    }
