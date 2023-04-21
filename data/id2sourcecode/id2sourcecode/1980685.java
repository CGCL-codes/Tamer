    public static void postUrl(String targetUrl, HashMap<String, String> parameters) {
        try {
            String data = "";
            for (String key : parameters.keySet()) {
                data += (data.length() > 0) ? "&" : "";
                data += URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(parameters.get(key), "UTF-8");
            }
            URL url = new URL(targetUrl);
            URLConnection conn = url.openConnection();
            conn.setDoOutput(true);
            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(data);
            wr.flush();
            BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            wr.close();
            rd.close();
        } catch (Exception e) {
        }
    }
