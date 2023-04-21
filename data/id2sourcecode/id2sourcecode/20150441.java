    public void accessURL(String url, File file) throws Throwable {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        byte[] data = EntityUtils.toByteArray(response.getEntity());
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data);
        fos.flush();
        fos.close();
        handleHeaders(response.getAllHeaders());
    }
