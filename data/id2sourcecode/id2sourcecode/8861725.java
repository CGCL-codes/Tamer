    private String requestRandomKey() {
        String url = "http://192.168.1.16:8080/ufle/getmountkey.jsp?username=kevin&sms=374513";
        HttpClient httpClient = new DefaultHttpClient();
        HttpPost httpPost = new HttpPost(url);
        String line = null;
        try {
            HttpResponse httpResponse = httpClient.execute(httpPost);
            if (httpResponse.getStatusLine().getStatusCode() == HttpStatus.SC_OK) {
                line = readResponseFromRandomKey(httpResponse);
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            e.printStackTrace();
        } catch (IOException e) {
            Log.e(TAG, "Could not establish a HTTP connection to the server or could not get a response properly from the server.", e);
            e.printStackTrace();
        }
        return line;
    }
