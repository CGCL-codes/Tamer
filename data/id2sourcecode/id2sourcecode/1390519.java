    public String getContent(String url) throws MalformedURLException, IOException {
        URLConnection httpConnection = new URL(url).openConnection();
        httpConnection.connect();
        BufferedInputStream bis = new BufferedInputStream(httpConnection.getInputStream());
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int bytesRead;
        while (-1 != (bytesRead = bis.read(buff, 0, buff.length))) {
            baos.write(buff, 0, bytesRead);
        }
        return baos.toString("utf-8");
    }
