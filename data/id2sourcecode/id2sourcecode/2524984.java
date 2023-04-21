    private void httpPost(URL url, HashMap options) {
        HttpURLConnection conn = null;
        try {
            String postdata = "";
            if (options != null) {
                StringBuilder builder = new StringBuilder();
                Iterator it = options.keySet().iterator();
                boolean firstLine = true;
                while (it.hasNext()) {
                    String key = (String) it.next();
                    String value = (String) options.get(key);
                    String value_encoded = URLEncoder.encode(value, "UTF-8");
                    if (!firstLine) builder.append('&'); else firstLine = false;
                    builder.append(key + "=" + value_encoded);
                }
                postdata = builder.toString();
            }
            Log.d("Blog", "postdata:" + postdata);
            conn = (HttpURLConnection) url.openConnection();
            conn.setReadTimeout(10 * 1000);
            conn.setConnectTimeout(15 * 1000);
            conn.setRequestMethod("POST");
            conn.setDoInput(true);
            conn.setDoOutput(true);
            if (postdata.length() > 0) {
                OutputStreamWriter writer = new OutputStreamWriter(conn.getOutputStream(), "UTF-8");
                writer.write(postdata);
                writer.flush();
                writer.close();
            }
            conn.connect();
            InputStreamReader reader = new InputStreamReader(conn.getInputStream(), "UTF-8");
            reader.close();
        } catch (IOException e) {
        } finally {
            if (conn != null) conn.disconnect();
        }
    }
