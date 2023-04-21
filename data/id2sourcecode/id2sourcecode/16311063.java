    public static String getAccount(String accountUrl, String session, String securityID, String firstname, String lastname, String address, String email, String username, String password, String eula) {
        HttpGet get = new HttpGet(accountUrl + ";ZoneID=1;PartnerID=0;ApplicationID=VoipDroid;OsType=Android;SessionID=" + session + ";SecurityID=" + securityID + ";FirstName=" + firstname + ";LastName=" + lastname + ";Address=" + address + ";CountryID=1;Password=" + password + ";Email=" + email + ";DeviceType=AndroidDevPhone1;UsernameString=" + username + ";CallingAppVersion=1.0;InstalledCoreVersion=1.0;EULA=" + eula);
        DefaultHttpClient httpclient = new DefaultHttpClient();
        String result = new String();
        try {
            HttpResponse response = httpclient.execute(get);
            HttpEntity entity = response.getEntity();
            Log.v("httpGet", "Login form get: " + response.getStatusLine());
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
