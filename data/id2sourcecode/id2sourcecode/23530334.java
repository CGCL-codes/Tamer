    public String webPost(String methodName, ArrayList<NameValuePair> params) {
        InputStream is = null;
        String result = null;
        boolean error = false;
        try {
            httpPost = new HttpPost(webServiceUrl + methodName);
            httpPost.setEntity(new UrlEncodedFormEntity(params));
            HttpResponse response = httpClient.execute(httpPost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            error = true;
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        if (!error) {
            try {
                BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
                StringBuilder sb = new StringBuilder();
                String line = null;
                while ((line = reader.readLine()) != null) sb.append(line + "\n");
                is.close();
                result = sb.toString();
            } catch (Exception e) {
                error = true;
                Log.e("log_tag", "Error converting result " + e.toString());
            }
        }
        if (error) return "connection_error"; else return result;
    }
