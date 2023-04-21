    public static FBUserInfo getUserInfo(String token, String user) {
        FBUserInfo info = new FBUserInfo();
        try {
            String queryparms = "access_token=" + URLEncoder.encode(token, "UTF-8");
            URL url = new URL("https://graph.facebook.com/" + user + "?" + queryparms);
            URLConnection conn = url.openConnection();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            JSONParser p = new JSONParser();
            JSONObject ob = (JSONObject) p.parse(rd);
            info.setName((String) ob.get("name"));
            info.setGender((String) ob.get("gender"));
            rd.close();
        } catch (Exception err) {
            err.printStackTrace();
        }
        return info;
    }
