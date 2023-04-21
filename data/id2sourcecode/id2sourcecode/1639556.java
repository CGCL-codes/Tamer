    public static void main(String[] args) throws Exception {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpGet httpget = new HttpGet("http://adgeo.163.com/ad_cookies");
        HttpResponse response = httpclient.execute(httpget);
        HttpEntity entity = response.getEntity();
        System.out.println("Login form get: " + response.getStatusLine());
        if (entity != null) {
            entity.consumeContent();
        }
        System.out.println("Initial set of cookies:");
        List<Cookie> cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }
        HttpPost httpost = new HttpPost("http://reg.163.com/login.jsp?type=1&product=mail163&url=http://entry.mail.163.com/coremail/fcg/ntesdoor2?lightweight%3D1%26verifycookie%3D1%26language%3D-1%26style%3D-1");
        List<NameValuePair> data = new ArrayList<NameValuePair>(10);
        data.add(new BasicNameValuePair("verifycookie", "1"));
        data.add(new BasicNameValuePair("style", "-1"));
        data.add(new BasicNameValuePair("product", "mail163"));
        data.add(new BasicNameValuePair("username", "jn9211"));
        data.add(new BasicNameValuePair("password", "ilike8ds"));
        data.add(new BasicNameValuePair("savelogin", ""));
        data.add(new BasicNameValuePair("selType", "-1"));
        httpost.setEntity(new UrlEncodedFormEntity(data, HTTP.UTF_8));
        response = httpclient.execute(httpost);
        entity = response.getEntity();
        System.out.println("Login form get: " + response.getStatusLine());
        if (entity != null) {
            entity.consumeContent();
        }
        System.out.println("Post logon cookies:");
        cookies = httpclient.getCookieStore().getCookies();
        if (cookies.isEmpty()) {
            System.out.println("None");
        } else {
            for (int i = 0; i < cookies.size(); i++) {
                System.out.println("- " + cookies.get(i).toString());
            }
        }
        httpclient.getConnectionManager().shutdown();
    }
