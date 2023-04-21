    @SuppressWarnings("deprecation")
    public static String getStringByPOST(CommonRequest request) {
        try {
            int timeout = request.getTimeout();
            HttpParams httpParameters = new BasicHttpParams();
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
            HttpConnectionParams.setSoTimeout(httpParameters, timeout);
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            HttpPost httppost = new HttpPost(request.getUrl());
            List<NameValuePair> nameValuePairs = new ArrayList<NameValuePair>(2);
            for (int i = 0; i < request.sizeListParameter(); i++) {
                RequestParameter parameter = request.getRequestParameter(i);
                String key = parameter.getKey();
                String value = URLEncoder.encode(parameter.getValue());
                BasicNameValuePair pair = new BasicNameValuePair(key, value);
                nameValuePairs.add(pair);
            }
            httppost.setEntity(new UrlEncodedFormEntity(nameValuePairs));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            InputStreamReader input = new InputStreamReader(is, "iso-8859-1");
            BufferedReader reader = new BufferedReader(input, 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line + "\n");
            }
            is.close();
            return sb.toString();
        } catch (Exception e) {
            return "{\"status\":\"false\", \"message\":\"Connect time out!\",\"error\":\"Connect time out!\"}";
        }
    }
