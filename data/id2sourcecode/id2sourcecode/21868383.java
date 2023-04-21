    @Override
    public TokenOAuthWrap getNewToken(TokenOAuthWrap token) {
        try {
            URL url = new URL(getRefreshTokenURL());
            URLConnection conn = url.openConnection();
            conn.setRequestProperty("Content Type", "application/x-www-form-urlencoded");
            String data = URLEncoder.encode("wrap_refresh_token", "UTF-8") + "=" + token.getRefreshToken();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            String responseData = "";
            token.setAccessToken("");
            while ((line = rd.readLine()) != null) {
                responseData += line;
            }
            Map<String, String> params = OAuthUtils.buildMapFromQueryResponse(responseData);
            token.setExpirationTime(Integer.parseInt(params.get("wrap_access_token_expires_in")));
            token.setAccessToken(params.get("wrap_access_token"));
            rd.close();
        } catch (Exception e) {
            throw new JavaOpenAuthException("Error when accessing hotmail request token url", e);
        }
        return token;
    }
