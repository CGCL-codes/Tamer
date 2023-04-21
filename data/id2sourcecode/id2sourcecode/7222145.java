    public static void addContact(String username, String password, String jid, String nick) {
        DefaultHttpClient httpclient = new DefaultHttpClient();
        HttpPost post = new HttpPost("https://g5data04.gizmo5.com/xmlfeed/app?");
        List<NameValuePair> nvps = new ArrayList<NameValuePair>();
        nvps.add(new BasicNameValuePair("class", "Subscriber"));
        nvps.add(new BasicNameValuePair("proc", "addContact"));
        nvps.add(new BasicNameValuePair("skip_redirect", "1"));
        nvps.add(new BasicNameValuePair("xmlfeed_version", "1"));
        nvps.add(new BasicNameValuePair("partnerId", "0"));
        nvps.add(new BasicNameValuePair("raw", "1"));
        nvps.add(new BasicNameValuePair("username", username));
        nvps.add(new BasicNameValuePair("password", password));
        nvps.add(new BasicNameValuePair("jid", jid));
        nvps.add(new BasicNameValuePair("nickName", nick));
        try {
            post.setEntity(new UrlEncodedFormEntity(nvps, HTTP.UTF_8));
            HttpResponse response = httpclient.execute(post);
            HttpEntity entity = response.getEntity();
            Log.v("httpPost", "Login form get: " + response.getStatusLine());
            try {
                SAXParserFactory spf = SAXParserFactory.newInstance();
                SAXParser sp = spf.newSAXParser();
                XMLReader xr = sp.getXMLReader();
                ContactHandler myContactHandler = new ContactHandler();
                xr.setContentHandler(myContactHandler);
                xr.parse(new InputSource(entity.getContent()));
            } catch (Exception e) {
                Log.v("PARSE", "Error: " + e.getMessage());
            }
            if (entity != null) {
                entity.consumeContent();
            }
        } catch (UnsupportedEncodingException e) {
            Log.e("ERRORE HTTP", e.getMessage());
        } catch (ClientProtocolException e) {
            Log.e("ERRORE HTTP", e.getMessage());
        } catch (IOException e) {
            Log.e("ERRORE HTTP", e.getMessage());
        }
    }
