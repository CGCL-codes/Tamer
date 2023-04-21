    public static void receiveFileFromSocket(ServerSocket ss, File f) throws Exception {
        Socket sock = null;
        FileOutputStream fos = null;
        InputStream is = null;
        try {
            ss.setSoTimeout(5000);
            sock = ss.accept();
            if (sock == null) {
                throw new Exception("No socket connection received");
            }
            File dir = f.getParentFile();
            if (dir != null) {
                dir.mkdirs();
            }
            fos = new FileOutputStream(f);
            is = sock.getInputStream();
            byte[] b = new byte[1024];
            int bytesread = -1;
            while ((bytesread = is.read(b, 0, b.length)) != -1) {
                fos.write(b, 0, bytesread);
            }
        } finally {
            CloseUtils.close(fos);
            CloseUtils.close(is);
            CloseUtils.close(sock);
            CloseUtils.close(ss);
        }
    }
