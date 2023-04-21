    public String get(String url) {
        StringBuilder result = null;
        try {
            Log.d(tag, "executing request " + url);
            HttpGet httpget = new HttpGet(url);
            HttpResponse response = httpclient.execute(httpget, localContext);
            HttpEntity entity = response.getEntity();
            List<Cookie> cookies = cookieStore.getCookies();
            for (int i = 0; i < cookies.size(); i++) {
                Log.d(tag, "Local cookie: " + cookies.get(i));
            }
            if (entity != null) {
                BufferedReader in = new BufferedReader(new InputStreamReader(response.getEntity().getContent()));
                result = new StringBuilder();
                String str;
                while ((str = in.readLine()) != null) {
                    result.append(str);
                }
                in.close();
                Log.d(tag, "return response " + result.toString());
            }
        } catch (Exception e) {
            Log.e(tag, e.getMessage());
        }
        return (result == null) ? null : result.toString();
    }
