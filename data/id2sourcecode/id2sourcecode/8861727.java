    private String requestMountKey(String url, String username, String randomKey) {
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        List<NameValuePair> params = new ArrayList<NameValuePair>();
        params.add(new BasicNameValuePair("username", username));
        params.add(new BasicNameValuePair("sms", randomKey));
        String line = null;
        try {
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                line = readResponseFromMountKey(httpResponse);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "Could not establish a HTTP connection to the server or could not get a response properly from the server.", e);
            e.printStackTrace();
        }
        String mountkey = line;
        return mountkey;
    }
