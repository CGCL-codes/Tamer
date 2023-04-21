    public void cleanUp() throws IOException {
        try {
            HttpPost httppost = new HttpPost(remoteURL);
            List<NameValuePair> nameValuePairList = new ArrayList<NameValuePair>();
            nameValuePairList.add(new BasicNameValuePair("cmd", "logout"));
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairList, HTTP.UTF_8));
            System.out.println("executing logout request " + httppost.getRequestLine());
            HttpResponse response = httpClient.execute(httppost, localContext);
            if (response.getStatusLine().getStatusCode() / 200 != 1) {
                throw new IOException("Unable to logout");
            }
        } catch (Exception e) {
            throw new IOException(e);
        }
        httpClient.getConnectionManager().shutdown();
    }
