    public static synchronized void sendObjectViaHTTPRequest(URL url, Object object) throws Exception {
        HttpURLConnection init = (HttpURLConnection) url.openConnection();
        init.setConnectTimeout(READ_TIME_OUT);
        init.setReadTimeout(READ_TIME_OUT);
        init.setUseCaches(false);
        init.setDoOutput(true);
        init.setRequestProperty("Content-type", "application/octet-stream");
        ObjectOutputStream out = new ObjectOutputStream(init.getOutputStream());
        out.writeObject(object);
        out.flush();
        init.getResponseCode();
        System.out.println("http:" + init.getResponseMessage());
    }
