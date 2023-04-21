    public void getFile(File f) throws IOException {
        sendRequest();
        BufferedInputStream is = new BufferedInputStream(conn.getInputStream());
        BufferedOutputStream os = new BufferedOutputStream(new FileOutputStream(f));
        byte[] buf = new byte[BUF_LEN];
        for (int n; (n = is.read(buf, 0, BUF_LEN)) > 0; ) os.write(buf, 0, n);
        try {
            is.close();
        } catch (Exception e) {
        }
        try {
            os.close();
        } catch (Exception e) {
        }
    }
