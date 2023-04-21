    protected InputStream post(String loc, HashMap params) throws IOException {
        Iterator iter = params.keySet().iterator();
        StringBuffer data = new StringBuffer();
        while (iter.hasNext()) {
            String key = (String) iter.next();
            String value;
            if (params.get(key) instanceof String) value = (String) params.get(key); else value = ((ServiceParameter) params.get(key)).getValue();
            data.append(URLEncoder.encode(key, "UTF-8") + "=" + URLEncoder.encode(value, "UTF-8"));
            if (iter.hasNext()) data.append("&");
        }
        URL url = new URL(loc + "?" + data.toString());
        System.out.println("QUerying service: " + url.toString());
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setFollowRedirects(true);
        conn.connect();
        int code = conn.getResponseCode();
        if (code >= 200 && code <= 204) {
            return conn.getInputStream();
        } else {
            return new StringBufferInputStream("<workbench:object xmlns:workbench='http://www.oriel.org/workbench'><workbench:empty-result/></workbench:object>");
        }
    }
