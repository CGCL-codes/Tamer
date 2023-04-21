    public Object create(Class api, String urlName) throws MalformedURLException {
        if (api == null) throw new NullPointerException();
        URL url = new URL(urlName);
        try {
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setConnectTimeout(10);
            conn.setReadTimeout(10);
            conn.setRequestProperty("Connection", "close");
            InputStream is = conn.getInputStream();
            is.close();
            conn.disconnect();
        } catch (IOException e) {
        }
        BurlapProxy handler = new BurlapProxy(this, url);
        return Proxy.newProxyInstance(api.getClassLoader(), new Class[] { api, BurlapRemoteObject.class }, handler);
    }
