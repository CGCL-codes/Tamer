    public static Xauth4SinaData xauth4Sina(String userName, String passWord) throws Exception {
        OAuthConsumer consumer = new DefaultOAuthConsumer("3811434321", "ee1168b97cb7093e752a236adc9b29b3");
        URL url = new URL("http://api.t.sina.com.cn/oauth/access_token");
        System.out.println(url);
        HttpURLConnection request = (HttpURLConnection) url.openConnection();
        request.setDoOutput(true);
        request.setRequestMethod("POST");
        HttpParameters para = new HttpParameters();
        para.put("x_auth_username", userName);
        para.put("x_auth_password", passWord);
        para.put("x_auth_mode", "client_auth");
        consumer.setAdditionalParameters(para);
        consumer.sign(request);
        OutputStream ot = request.getOutputStream();
        ot.write(("x_auth_username=" + userName + "&x_auth_password=" + passWord + "&x_auth_mode=client_auth").getBytes());
        ot.flush();
        ot.close();
        request.connect();
        System.out.println(request.getResponseCode());
        if (request.getResponseCode() == 200) {
            String responseStr = null;
            InputStream inputStream = request.getInputStream();
            int len = 0;
            byte[] buffer = new byte[1024];
            while ((len = inputStream.read(buffer)) != -1) {
                responseStr = new String(buffer, 0, len);
            }
            String accessToken = responseStr.substring(responseStr.indexOf("oauth_token=") + 12, responseStr.indexOf("&oauth_token_secret"));
            String accessTokenSecret = responseStr.substring(responseStr.indexOf("&oauth_token_secret=") + 20, responseStr.indexOf("&user_id="));
            String userId = responseStr.substring(responseStr.indexOf("&user_id=") + 9);
            Xauth4SinaData xauth4SinaData = new Xauth4SinaData();
            xauth4SinaData.setAccessToken(accessToken);
            xauth4SinaData.setAccessTokenSecret(accessTokenSecret);
            xauth4SinaData.setUserId(userId);
            return xauth4SinaData;
        }
        request.disconnect();
        return null;
    }
