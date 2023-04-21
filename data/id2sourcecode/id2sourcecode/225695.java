    public static void main(String[] args) {
        try {
            if ((args.length != 1) && (args.length != 2)) throw new IllegalArgumentException("Wrong number of arguments");
            OutputStream to_file;
            if (args.length == 2) to_file = new FileOutputStream(args[1]); else to_file = System.out;
            URL url = new URL(args[0]);
            String protocol = url.getProtocol();
            if (!protocol.equals("http")) throw new IllegalArgumentException("URL must use 'http:' protocol");
            String host = url.getHost();
            int port = url.getPort();
            if (port == -1) port = 80;
            String filename = url.getFile();
            Socket socket = new Socket(host, port);
            InputStream from_server = socket.getInputStream();
            PrintWriter to_server = new PrintWriter(new OutputStreamWriter(socket.getOutputStream()));
            String headers = "GET " + filename + " HTTP/1.1 \n" + "Host: " + host + "\n" + "Accept: text/html \n";
            to_server.println("GET " + filename);
            to_server.flush();
            byte[] buffer = new byte[4096];
            int bytes_read;
            while ((bytes_read = from_server.read(buffer)) != -1) to_file.write(buffer, 0, bytes_read);
            socket.close();
            to_file.close();
        } catch (Exception e) {
            System.err.println(e);
            System.err.println("Usage: java HttpClient <URL> [<filename>]");
        }
    }
