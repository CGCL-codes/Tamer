    public void sendRegistrationIdToServer(String deviceId, String registrationId) {
        Log.v("C2DM", "Sending registration ID to my application server");
        HttpClient client = SQLiteBackup.authClient;
        HttpPost post = new HttpPost("http://3dforandroid.appspot.com/api/v2/register");
        String responseMessage = null;
        try {
            String jsonString = "{\"deviceid\":\"" + deviceId + "\",\"registrationid\":\"" + registrationId + "\"}";
            StringEntity se = new StringEntity(jsonString);
            se.setContentEncoding(HTTP.UTF_8);
            se.setContentType("application/json");
            post.setEntity(se);
            post.setHeader("Content-Type", "application/json");
            post.setHeader("Accept", "*/*");
            HttpResponse response = client.execute(post);
            HttpEntity entity = response.getEntity();
            InputStream instream;
            instream = entity.getContent();
            responseMessage = read(instream);
            Log.v("postresponse", responseMessage);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
