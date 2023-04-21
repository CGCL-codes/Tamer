    public static void writeFileToSocket(String host, int port, File f) throws Exception {
        FileInputStream fis = null;
        OutputStream os = null;
        Socket sock = null;
        try {
            sock = new Socket(host, port);
            fis = new FileInputStream(f);
            os = sock.getOutputStream();
            byte[] b = new byte[1024];
            int bytesread = -1;
            while ((bytesread = fis.read(b, 0, b.length)) != -1) {
                os.write(b, 0, bytesread);
            }
        } finally {
            CloseUtils.close(os);
            CloseUtils.close(fis);
            CloseUtils.close(sock);
        }
    }
