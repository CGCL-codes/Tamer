    private int doLogin(String email, String pass) {
        System.out.println("Target URL: " + loginPageUrl);
        try {
            HttpGet loginGet = new HttpGet(loginPageUrl);
            HttpResponse response = httpClient.execute(loginGet);
            HttpEntity entity = response.getEntity();
            System.out.println("Login form get: " + response.getStatusLine());
            if (entity != null) {
                entity.consumeContent();
            }
            System.out.println("Initial set of cookies:");
            List<Cookie> cookies = ((DefaultHttpClient) httpClient).getCookieStore().getCookies();
            if (cookies.isEmpty()) {
                System.out.println("None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    System.out.println("- " + cookies.get(i).toString());
                }
            }
            HttpPost httpost = new HttpPost(loginPageUrl);
            List<NameValuePair> nvps = new ArrayList<NameValuePair>();
            nvps.add(new BasicNameValuePair("email", email));
            nvps.add(new BasicNameValuePair("pass", pass));
            httpost.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse responsePost = httpClient.execute(httpost);
            entity = responsePost.getEntity();
            System.out.println("Login form get: " + responsePost.getStatusLine());
            if (entity != null) {
                entity.consumeContent();
            }
            System.out.println("Post logon cookies:");
            cookies = ((DefaultHttpClient) httpClient).getCookieStore().getCookies();
            if (cookies.isEmpty()) {
                System.out.println("None");
            } else {
                for (int i = 0; i < cookies.size(); i++) {
                    System.out.println("- " + cookies.get(i).toString());
                }
            }
        } catch (IOException ioe) {
            System.err.print("IOException");
            System.err.println(ioe.getMessage());
            ioe.printStackTrace();
            return ErrorCode.kError_Global_ValidationError;
        }
        return ErrorCode.Error_Global_NoError;
    }
