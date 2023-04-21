    public static boolean upload(InetAddress addr, int port, InputStream layout) throws IOException {
        Socket s = new Socket(addr, port);
        OutputStream os = s.getOutputStream();
        BufferedReader in = new BufferedReader(new InputStreamReader(s.getInputStream()));
        String line = in.readLine();
        if (line == null || line.startsWith("Denied")) {
            return false;
        }
        byte[] buff = new byte[1024];
        int read = layout.read(buff);
        while (read != -1) {
            os.write(buff, 0, read);
            read = layout.read(buff);
        }
        os.flush();
        s.close();
        return true;
    }
