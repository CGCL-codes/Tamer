    public static void main(String[] args) throws IOException {
        if (args.length < 3) {
            System.err.println("Usage: GZIPTestClient dest port file");
            System.exit(1);
        }
        InetAddress dest = InetAddress.getByName(args[0]);
        int port = Integer.parseInt(args[1]);
        Socket socket = new Socket(dest, port);
        GZIPOutputStream out = new GZIPOutputStream(socket.getOutputStream());
        FileInputStream in = new FileInputStream(args[2]);
        byte[] buf = new byte[8192];
        int nread;
        while ((nread = in.read(buf)) > 0) {
            out.write(buf, 0, nread);
        }
        out.finish();
        out.flush();
        in.close();
        socket.close();
    }
