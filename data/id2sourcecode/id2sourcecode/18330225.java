    public void postData(String url, List<NameValuePair> nameValuePairs) {
        HttpClient httpclient = new DefaultHttpClient();
        HttpPost httppost = new HttpPost(url);
        try {
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            Header[] headers = response.getAllHeaders();
            for (int i = 0; i < headers.length; i++) {
                Log.i(TAG, "HEADER: " + headers[i].getName() + " - " + headers[i].getValue());
            }
            InputStream is = response.getEntity().getContent();
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            StringBuilder sb = new StringBuilder();
            String line = "";
            String gtinCode = null;
            while ((line = reader.readLine()) != null) {
                System.out.println("Parsing line... " + line);
                if (line.contains("<html xmlns:fn")) {
                    gtinCode = line.substring(line.indexOf("GLN:") + 165, line.indexOf("GLN:") + 176);
                    System.out.println("OUT: " + gtinCode);
                    break;
                }
            }
            Log.i(TAG, "OK");
        } catch (ClientProtocolException e) {
            Log.e(TAG, "ClientProtocolException ", e);
        } catch (IOException e) {
            Log.e(TAG, "HTTP Not Available", e);
        }
    }
