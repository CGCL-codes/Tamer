    @Override
    public TokenOAuthWrap getTokens(String verificationCode) {
        TokenOAuthWrap token = new TokenOAuthWrap();
        try {
            URL url = new URL(getTokenURL());
            URLConnection conn = url.openConnection();
            String data = URLEncoder.encode("wrap_verification_code", "UTF-8") + "=" + URLEncoder.encode(verificationCode, "UTF-8");
            data += "&" + URLEncoder.encode("wrap_client_id", "UTF-8") + "=" + URLEncoder.encode(getConsumerKey(), "UTF-8");
            data += "&" + URLEncoder.encode("wrap_client_secret", "UTF-8") + "=" + URLEncoder.encode(getConsumerSecret(), "UTF-8");
            data += "&" + URLEncoder.encode("wrap_callback", "UTF-8") + "=" + getCallbackUrl();
            data += "&" + URLEncoder.encode("idtype", "UTF-8") + "=" + URLEncoder.encode("CID", "UTF-8");
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String line;
            while ((line = rd.readLine()) != null) {
                Map<String, String> params = OAuthUtils.buildMapFromQueryResponse(line);
                token.setExpirationTime(Integer.parseInt(params.get("wrap_access_token_expires_in")));
                token.setRefreshToken(params.get("wrap_refresh_token"));
                token.setAccessToken(params.get("wrap_access_token"));
                token.setUserId(params.get("uid"));
                token.setSecretKey(params.get("skey"));
            }
            rd.close();
        } catch (Exception e) {
            throw new JavaOpenAuthException("Error when accessing hotmail request token url", e);
        }
        return token;
    }
