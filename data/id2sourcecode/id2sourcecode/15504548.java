    public void download() {
        if (place == SgfPlace.DISK || place == SgfPlace.NO || place == SgfPlace.UNKNOWN) {
            return;
        }
        System.out.println("SgfFile:download");
        HttpURLConnection connection = null;
        URL url;
        InputStream in = null;
        OutputStream out = null;
        try {
            url = new URL(origStr);
        } catch (MalformedURLException e) {
            System.out.println("SgfFile:MalformedURLException :" + origStr);
            return;
        }
        try {
            byte[] buf = new byte[4096];
            int size = 0;
            connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(10 * 1000);
            connection.setReadTimeout(10 * 1000);
            connection.connect();
            in = connection.getInputStream();
            out = new FileOutputStream(file);
            while ((size = in.read(buf)) != -1) {
                out.write(buf, 0, size);
            }
            out.flush();
            in.close();
            out.close();
            place = SgfPlace.DISK;
        } catch (SocketTimeoutException e) {
            System.out.println("SgfFile download : SocketTimeoutException:" + e);
        } catch (IOException e) {
            System.out.println("SgfFile download : IOException:" + e);
        } finally {
            if (connection != null) {
                connection.disconnect();
            }
        }
    }
