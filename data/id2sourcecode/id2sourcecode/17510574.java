    public static void loginAmazoncloudDrive() throws Exception {
        HttpParams params = new BasicHttpParams();
        params.setParameter("http.useragent", "Mozilla/5.0 (Windows; U; Windows NT 6.1; en-GB; rv:1.9.2) Gecko/20100115 Firefox/3.6");
        DefaultHttpClient httpclient = new DefaultHttpClient(params);
        System.out.println("Trying to log in to amazon.com");
        HttpPost httppost = new HttpPost("https://www.amazon.com/ap/signin");
        httppost.setHeader("Cookie", cookies.toString());
        httppost.setHeader("x-amz-id-2", amzid);
        List<NameValuePair> formparams = new ArrayList<NameValuePair>();
        formparams.add(new BasicNameValuePair("appActionToken", appactiontoken));
        formparams.add(new BasicNameValuePair("appAction", appaction));
        formparams.add(new BasicNameValuePair("openid.pape.max_auth_age", authage));
        formparams.add(new BasicNameValuePair("openid.ns", openid_ns));
        formparams.add(new BasicNameValuePair("openid.ns.pape", openid_ns_pape));
        formparams.add(new BasicNameValuePair("pageId", pageid));
        formparams.add(new BasicNameValuePair("openid.identity", identity));
        formparams.add(new BasicNameValuePair("openid.claimed_id", claimedid));
        formparams.add(new BasicNameValuePair("openid.mode", mode));
        formparams.add(new BasicNameValuePair("openid.assoc_handle", handle));
        formparams.add(new BasicNameValuePair("openid.return_to", returnto));
        formparams.add(new BasicNameValuePair("email", "007007dinesh@gmail.com"));
        formparams.add(new BasicNameValuePair("create", "0"));
        formparams.add(new BasicNameValuePair("password", "*******************"));
        formparams.add(new BasicNameValuePair("x", "0"));
        formparams.add(new BasicNameValuePair("y", "0"));
        formparams.add(new BasicNameValuePair("metadata1", "z2852AgFvFLdGzUwuI%2Bx%2BDt2CtT%2BIx1RZDxHQl%2Bj%2FmijzBdMoQDpfeG9V4RskgUuq%2FrdkiqgoirJTTUZn9UjqAHIvZ0ifw3PD040242h9v0SCcivb1prXNDdqf5uZRdQMDcooVNvdr2%2FxIY9DrykC44oUU0jKzkpyV4Ebph1BIp47z1nwUQEIuGbWnFziWOhrOkrdH%2BAFKIGz%2B%2FgeMTiyjhFUtGPJYTbg%2BLQY7MSwAlzKowZFTXCU9j6gjDw%2FczgcLQ1Ng4hcYaMGnMi57nsl3lF2cluhkUp7bJXj0piUa9QUAFCC63ISgoRoa4wv33bHG6HIWu68q%2BOGcoOE94TSZuu4kwd8KRals4%2FRNw7IBTwoclqXKq9GKxfVeIkCegpZeKZlwUmlQzOYaVhq72fwqMg6k3tFpMw%2FKoQuRNXda1u8bov46d%2FkfGHeCoW0S%2BsKog2CcIsEP%2Bkx33ailjqcMEyC8yUgoeVEpcXBcsDTXkEOldv7veVc6tmBRKHufN7z%2FioHUWPvgXti7MRhIeicFEJFdB9ArlI8cbyn5TsRADIpLq6rk1M59clcObDqeq1%2F5u%2BFNe4JsptAShiO%2Bg%2Fw%2BSFWASuBILwFg0i%2BjoAeXFyQ3QbcvQuOA5G%2BBy9b4AWWhoHxfjYp5WsNUTyeA1UIVQmWn5hzUUfko9TmaoY%2BGJ5BRS0kHR4AWgH%2B3xTwVBDG6cK559O8Zdy6FymADx5XPwmPXG7iHW9JmrS3yriHIUV%2Bz4%2FcfOuW0Yovn6FVTpkn8WRrz5AOt3AnH4L9SdqW9gok9mdl3Nm7Bl67jml%2F%2F0mccIwfbeoug%3D%3D"));
        UrlEncodedFormEntity entity = new UrlEncodedFormEntity(formparams, "UTF-8");
        httppost.setEntity(entity);
        HttpResponse httpresponse = httpclient.execute(httppost);
        System.out.println("response");
        Header[] allHeaders = httpresponse.getAllHeaders();
        for (int i = 0; i < allHeaders.length; i++) {
            System.out.println(allHeaders[i].getName() + " : " + allHeaders[i].getValue());
        }
        System.out.println(EntityUtils.toString(httpresponse.getEntity()));
        System.out.println("Getting cookies........");
        Iterator<Cookie> it = httpclient.getCookieStore().getCookies().iterator();
        Cookie escookie = null;
        while (it.hasNext()) {
            escookie = it.next();
            System.out.println(escookie.getName() + " : " + escookie.getValue());
        }
    }
