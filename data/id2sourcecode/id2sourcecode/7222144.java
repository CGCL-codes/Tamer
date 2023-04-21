    public static boolean getContactsMD5(String username, String password) {
        HttpGet get = new HttpGet("https://g5data04.gizmo5.com/xmlfeed/app?class=Subscriber;proc=getContacts;skip_redirect=1;xmlfeed_version=1;partnerId=0;raw=1;username=" + username + ";password=" + password + ";md5=1;md5Only=1");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        try {
            HttpResponse response = httpclient.execute(get);
            HttpEntity entity = response.getEntity();
            Log.v("httpGet", "Login form get: " + response.getStatusLine());
            int status = response.getStatusLine().getStatusCode();
            if (status != 404) {
                try {
                    SAXParserFactory spf = SAXParserFactory.newInstance();
                    SAXParser sp = spf.newSAXParser();
                    XMLReader xr = sp.getXMLReader();
                    ContactHandler myContactHandler = new ContactHandler();
                    xr.setContentHandler(myContactHandler);
                    xr.parse(new InputSource(entity.getContent()));
                    contactMD5 = myContactHandler.getParsedMd5();
                } catch (Exception e) {
                    Log.v("PARSECONTACT", "Error: " + e.getMessage());
                    return false;
                }
            } else {
                Log.v("404", "Error: no resource in the given URL");
                return false;
            }
            if (entity != null) {
                entity.consumeContent();
            }
        } catch (Exception e) {
            Log.e("Error:", e.getMessage());
        } finally {
            get = null;
        }
        return true;
    }
