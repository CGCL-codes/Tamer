    private int connectAndLogin(String email, String pass) {
        logger.trace("Facebook: =========connectAndLogin begin===========");
        try {
            HttpPost httpost = new HttpPost(loginPageUrl);
            httpost.addHeader("Cookie", "lsd=abcde; test_cookie=1");
            httpost.addHeader("User-Agent", "Opera/9.50 (Windows NT 5.1; U; en-GB)");
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("email", email));
            nvps.add(new BasicNameValuePair("pass", pass));
            nvps.add(new BasicNameValuePair("charset_test", "€,´,€,´,水,Д,Є"));
            nvps.add(new BasicNameValuePair("locale", "en_US"));
            nvps.add(new BasicNameValuePair("pass_placeHolder", "Password"));
            nvps.add(new BasicNameValuePair("persistent", "1"));
            nvps.add(new BasicNameValuePair("lsd", "abcde"));
            nvps.add(new BasicNameValuePair("login", "Login"));
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            logger.info("Facebook: @executing post method to:" + loginPageUrl);
            HttpResponse loginPostResponse = facebookHttpClient.getHttpClient().execute(httpost);
            HttpEntity entity = loginPostResponse.getEntity();
            logger.trace("Facebook: Login form post: " + loginPostResponse.getStatusLine());
            if (entity != null) {
                logger.trace("Facebook: " + EntityUtils.toString(entity));
                entity.consumeContent();
            } else {
                logger.error("Facebook: Error: login post's response entity is null");
                return FacebookErrorCode.kError_Login_GenericError;
            }
            logger.trace("Facebook: Post logon cookies:");
            List<Cookie> cookies = facebookHttpClient.getCookies();
            if (cookies.isEmpty()) {
                logger.trace("Facebook: None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    logger.trace("Facebook: - " + cookies.get(i).toString());
                }
            }
            int statusCode = loginPostResponse.getStatusLine().getStatusCode();
            logger.info("Facebook: Post Method done(" + statusCode + ")");
            switch(statusCode) {
                case 100:
                    break;
                case 301:
                case 302:
                case 303:
                case 307:
                    {
                        Header[] headers = loginPostResponse.getAllHeaders();
                        for (int i = 0; i < headers.length; i++) {
                            logger.trace("Facebook: " + headers[i]);
                        }
                        Header locationHeader = loginPostResponse.getFirstHeader("location");
                        if (locationHeader != null) {
                            homePageUrl = locationHeader.getValue();
                            logger.info("Facebook: Redirect Location: " + homePageUrl);
                            if (homePageUrl == null || !homePageUrl.contains("facebook.com/home.php")) {
                                logger.error("Facebook: Login error! Redirect Location Url not contains \"facebook.com/home.php\"");
                                return FacebookErrorCode.kError_Login_GenericError;
                            }
                        } else {
                            logger.warn("Facebook: Warning: Got no redirect location.");
                        }
                    }
                    break;
                default:
                    ;
            }
            getPostFormIDAndFriends();
        } catch (IOException ioe) {
            logger.error("Facebook: IOException\n" + ioe.getMessage());
            return FacebookErrorCode.kError_Global_ValidationError;
        }
        logger.trace("Facebook: =========connectAndLogin end==========");
        return FacebookErrorCode.Error_Global_NoError;
    }
