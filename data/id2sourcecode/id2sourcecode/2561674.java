    private static String[] getQuestions() {
        InputStream is = null;
        String result = "";
        String domain = "http://www.pennquiz.com/users.php";
        ArrayList<NameValuePair> library = new ArrayList<NameValuePair>();
        library.add(new BasicNameValuePair("funct", "getQs"));
        try {
            HttpClient httpclient = new DefaultHttpClient();
            HttpPost httppost = new HttpPost(domain);
            httppost.setEntity(new UrlEncodedFormEntity(library));
            HttpResponse response = httpclient.execute(httppost);
            HttpEntity entity = response.getEntity();
            is = entity.getContent();
        } catch (Exception e) {
            Log.e("log_tag", "Error in http connection " + e.toString());
        }
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(is, "iso-8859-1"), 8);
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = reader.readLine()) != null) {
                sb.append(line);
            }
            is.close();
            result = sb.toString().substring(1, sb.length() - 1);
            return result.split(",");
        } catch (Exception e) {
            Log.e("log_tag", "Error converting result " + e.toString());
        }
        return null;
    }
