    public static void getOnlineImage(String server, String filepath, String filename) throws Exception {
        URL url = new URL(server);
        HttpURLConnection conn = (HttpURLConnection) url.openConnection();
        conn.setDoOutput(false);
        conn.setDoInput(true);
        conn.setRequestMethod("GET");
        conn.connect();
        InputStream in = conn.getInputStream();
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        byte[] buff = new byte[1024];
        int read = 0;
        while ((read = in.read(buff, 0, buff.length)) != -1) {
            bos.write(buff, 0, read);
        }
        File file = new File(filepath + File.separator + filename);
        if (file.exists()) {
            file.delete();
        }
        FileOutputStream fw = new FileOutputStream(file);
        fw.write(bos.toByteArray());
        fw.flush();
        fw.close();
        conn.disconnect();
    }
