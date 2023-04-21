    public static String getSession() {
        HttpGet get = new HttpGet("https://aps.plugndial.com/dll/app?class=DLL;proc=start;ZoneID=1;PartnerID=0;");
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String result = new String();
        try {
            HttpResponse response = httpclient.execute(get);
            HttpEntity entity = response.getEntity();
            Log.d("httpGet", "Login form get: " + response.getStatusLine());
            int status = response.getStatusLine().getStatusCode();
            if (status != 404) {
                try {
                    Reader reader = new InputStreamReader(entity.getContent());
                    int x;
                    int count = 0;
                    byte by[] = new byte[50];
                    while ((x = reader.read()) != -1) {
                        if (count < 50) {
                            by[count] = (byte) x;
                            result = result + (char) by[count];
                        }
                        count++;
                        if (count == 50) count = 0;
                    }
                } catch (Exception e) {
                    Log.v("PARSESESSION", "Error: " + e.getMessage());
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
        return result;
    }
