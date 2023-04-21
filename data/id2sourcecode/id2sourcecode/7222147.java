    public static void getProfile(String username, String password, String jid) {
        HttpGet get = new HttpGet("https://g5data04.gizmo5.com/xmlfeed/app?class=Subscriber;proc=getProfile;skip_redirect=1;xmlfeed_version=1;partnerId=0;raw=1;username=" + username + ";password=" + password + ";jid=" + jid);
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpResponse response = httpclient.execute(get);
            HttpEntity entity = response.getEntity();
            Log.v("httpGet", "Profile form get: " + response.getStatusLine());
            int status = response.getStatusLine().getStatusCode();
            if (status != 404) {
                try {
                    SAXParserFactory spf = SAXParserFactory.newInstance();
                    SAXParser sp = spf.newSAXParser();
                    XMLReader xr = sp.getXMLReader();
                    ProfileHandler myProfileHandler = new ProfileHandler();
                    xr.setContentHandler(myProfileHandler);
                    xr.parse(new InputSource(entity.getContent()));
                    profile = myProfileHandler.getParsedData();
                } catch (Exception e) {
                    Log.v("PARSEPROFILE", "Error: " + e.getMessage());
                }
            } else {
                Log.v("404", "Error: no resource in the given URL");
            }
            if (entity != null) {
                entity.consumeContent();
            }
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        } finally {
            get = null;
        }
    }
