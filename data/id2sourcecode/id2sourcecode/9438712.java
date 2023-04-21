    public TGBrowserResponse getResponse() throws Throwable {
        URL url = new URL(REMOTE_URL);
        URLConnection conn = url.openConnection();
        conn.setDoOutput(true);
        OutputStreamWriter outputStream = new OutputStreamWriter(conn.getOutputStream());
        outputStream.write(this.request);
        outputStream.flush();
        outputStream.close();
        return new TGBrowserResponse(conn.getInputStream());
    }
