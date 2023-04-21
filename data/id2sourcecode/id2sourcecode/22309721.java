    public void readStream(String strurl) throws Exception {
        URL url = new URL(strurl);
        HttpURLConnection huc = (HttpURLConnection) (url.openConnection());
        int code = huc.getResponseCode();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        if (code == 200) {
            InputStream is = huc.getInputStream();
            byte[] bytes = new byte[1024];
            int len;
            while ((len = is.read(bytes)) != -1) {
                out.write(bytes, 0, len);
            }
            out.close();
            is.close();
        } else {
            System.err.println("An error of type " + code + " occurred for:" + strurl);
        }
    }
