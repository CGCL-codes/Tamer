    public static String executeHttpPost() {
        BufferedReader in = null;
        try {
            HttpClient client = new DefaultHttpClient();
            HttpParams params = client.getParams();
            HttpConnectionParams.setConnectionTimeout(params, 1000);
            HttpPost request = new HttpPost("http://connectsoftware.com/mapi/publication/post");
            List<NameValuePair> postParameters = new ArrayList<NameValuePair>();
            postParameters.add(new BasicNameValuePair("format", "json"));
            postParameters.add(new BasicNameValuePair("token", "efc5c336fbc33f21ed5ea5ddb3bcb511"));
            postParameters.add(new BasicNameValuePair("limit", ""));
            postParameters.add(new BasicNameValuePair("start", ""));
            UrlEncodedFormEntity formEntity = new UrlEncodedFormEntity(postParameters);
            request.setEntity(formEntity);
            HttpResponse response = client.execute(request);
            in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
            StringBuffer sb = new StringBuffer("");
            String line = "";
            String NL = System.getProperty("line.separator");
            while ((line = in.readLine()) != null) {
                sb.append(line + NL);
            }
            in.close();
            String result = sb.toString();
            return result;
        } catch (Exception e) {
            return e.getMessage();
        }
    }
