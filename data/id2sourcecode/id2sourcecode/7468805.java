    public static void download(URL url, File dest, String username, String password) throws IOException {
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        con.setRequestMethod("GET");
        con.setDoInput(true);
        byte[] encodedPassword = (username + ":" + password).getBytes();
        BASE64Encoder encoder = new BASE64Encoder();
        con.setRequestProperty("Authorization", "Basic " + encoder.encode(encodedPassword));
        con.setConnectTimeout(2000);
        BufferedInputStream in = new BufferedInputStream(con.getInputStream());
        FileOutputStream fos = new FileOutputStream(dest);
        BufferedOutputStream bout = new BufferedOutputStream(fos, 1024);
        byte[] data = new byte[1024];
        int x = 0;
        while ((x = in.read(data, 0, 1024)) >= 0) {
            bout.write(data, 0, x);
        }
        bout.close();
        in.close();
        con.disconnect();
    }
