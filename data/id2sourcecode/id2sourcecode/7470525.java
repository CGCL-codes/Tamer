    public void get2file(String url, File file) throws Throwable {
        HttpGet httpGet = new HttpGet(url);
        HttpResponse response = client.execute(httpGet);
        byte[] data = EntityUtils.toByteArray(response.getEntity());
        if (file.exists()) file.delete();
        FileOutputStream fos = new FileOutputStream(file);
        fos.write(data);
        fos.flush();
        fos.close();
    }
