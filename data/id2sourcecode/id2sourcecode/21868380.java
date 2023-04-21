    @Override
    public String getContactsListJson(String accessToken, String userId) {
        String response = null;
        try {
            URL url = new URL(getContactsURL(userId));
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Accept", "application/json");
            conn.setRequestProperty("Content-Type", "application/json");
            conn.setRequestProperty("Authorization", "WRAP access_token=" + accessToken);
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                response = line;
            }
            rd.close();
        } catch (Exception e) {
            throw new JavaOpenAuthException("Error when accessing hotmail contact lists", e);
        }
        JSONObject jsonObject = JSONObject.fromObject(response);
        Integer code = OAuthUtils.getIntegerFromJson(jsonObject, "Code");
        if (code != null && code == 1062) {
            throw new InvalidOAuthTokenException("Invalid hotmail token, you need to refresh");
        }
        return response;
    }
