    private InputStream getRMPlayersXMLInputStream(String id, String key) throws IOException {
        String sid = ResourceBundleManager.getString(ResourceBundleManager.PROPS, "ID");
        String skey = ResourceBundleManager.getString(ResourceBundleManager.PROPS, "KEY");
        String rmpurl = ResourceBundleManager.getString(ResourceBundleManager.PROPS, "PLAYERS");
        rmpurl = rmpurl.replaceAll(sid, id).replaceAll(skey, key);
        URL url = new URL(rmpurl);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setRequestMethod("GET");
        conn.setDoInput(true);
        conn.setDoOutput(false);
        conn.setReadTimeout(10000);
        BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        StringBuilder sb = new StringBuilder();
        String line = null;
        while ((line = rd.readLine()) != null) {
            sb.append(line + '\n');
        }
        rd.close();
        byte[] bytes = sb.toString().getBytes("UTF-8");
        InputStream input = new ByteArrayInputStream(bytes);
        return input;
    }
