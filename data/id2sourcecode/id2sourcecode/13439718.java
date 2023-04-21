    public static void main(String[] args) {
        try {
            int port = 80;
            if (args.length > 0) port = new Integer(args[0]).intValue();
            ServerSocket ss = new ServerSocket(port);
            Socket s = ss.accept();
            InputStream is = s.getInputStream();
            OutputStream os = s.getOutputStream();
            int read = 0;
            while (read != -1) {
                read = is.read();
                if (read != -1) {
                    System.out.write(read);
                    System.out.flush();
                    os.write(read);
                    os.flush();
                }
            }
            System.out.println("\n\nclosed.");
            is.close();
            os.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
