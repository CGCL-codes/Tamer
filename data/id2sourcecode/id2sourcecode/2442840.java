    public String[] writeFileToSocket(String[] args) throws Exception {
        Socket sock = null;
        FileInputStream fis = null;
        OutputStream os = null;
        try {
            String filename = args[0];
            String host = args[1];
            int port = Integer.parseInt(args[2]);
            File f = new File(filename);
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
        return null;
    }
