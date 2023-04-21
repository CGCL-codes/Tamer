    @SuppressWarnings("deprecation")
    public static String getStringByGet(CommonRequest request) {
        try {
            HttpParams httpParameters = new BasicHttpParams();
            int timeout = request.getTimeout();
            HttpConnectionParams.setConnectionTimeout(httpParameters, timeout);
            HttpConnectionParams.setSoTimeout(httpParameters, timeout);
            HttpClient httpclient = new DefaultHttpClient(httpParameters);
            String url = request.getUrl();
            for (int i = 0; i < request.sizeListParameter(); i++) {
                RequestParameter parameter = request.getRequestParameter(i);
                String key = parameter.getKey();
                String value = URLEncoder.encode(parameter.getValue());
                if (i == 0) {
                    url += "?" + key + "=" + value;
                } else {
                    url += "&" + key + "=" + value;
                }
            }
            HttpGet httppost = new HttpGet(url);
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            InputStream is = entity.getContent();
            InputStreamReader input = new InputStreamReader(is);
            BufferedReader reader = new BufferedReader(input);
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
