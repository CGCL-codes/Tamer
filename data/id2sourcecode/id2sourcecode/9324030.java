    private File fetch(String urlString) throws MalformedURLException, IOException {
        URL url = new URL(urlString);
        String fileName = urlString.substring(urlString.lastIndexOf("/"));
        File imageStorage = Helper.getImagesDirectory();
        File image = new File(imageStorage, fileName);
        if (image.exists()) {
            return image;
        }
        FileOutputStream fos = new FileOutputStream(image);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.connect();
        byte[] buffer = new byte[25600];
        int len = -1;
        if (conn.getResponseCode() == 200) {
            InputStream is = conn.getInputStream();
            len = is.read(buffer, 0, 25600);
            while (len != -1) {
                fos.write(buffer, 0, len);
                len = is.read(buffer, 0, BUFFER_SIZE);
            }
        }
        conn.disconnect();
        fos.close();
        fos = null;
        return image;
    }
