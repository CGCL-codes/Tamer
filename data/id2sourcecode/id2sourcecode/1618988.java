    private boolean uploadSessions(String ymlBlob) {
        DefaultHttpClient client = new DefaultHttpClient();
        HttpPost method = new HttpPost(UploaderThread.ANALYTICS_URL);
        try {
            StringEntity postBody = new StringEntity(ymlBlob, "utf8");
            method.setEntity(postBody);
            HttpResponse response = client.execute(method);
            StatusLine status = response.getStatusLine();
            Log.v(UploaderThread.LOG_TAG, "Upload complete. Status: " + status.getStatusCode());
            return true;
        } catch (UnsupportedEncodingException e) {
            Log.v(LOG_TAG, "UnsuppEncodingException: " + e.getMessage());
            return false;
        } catch (ClientProtocolException e) {
            Log.v(LOG_TAG, "ClientProtocolException: " + e.getMessage());
            return false;
        } catch (IOException e) {
            Log.v(LOG_TAG, "IOException: " + e.getMessage());
            return false;
        }
    }
