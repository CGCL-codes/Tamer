    public void testServer(final int port) {
        (new Thread() {

            public void run() {
                try {
                    System.out.println("Test Server Start");
                    ServerSocket s = new ServerSocket(port);
                    Socket socket = s.accept();
                    PrintWriter out = new PrintWriter(socket.getOutputStream(), true);
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    System.out.println("Test Server Connection Accepted");
                    try {
                        int read;
                        while ((read = in.read()) != -1) {
                            System.out.println("TS: " + read);
                            out.write(read);
                            Thread.yield();
                        }
                    } catch (Exception e) {
                        System.out.println("Error: " + e.toString());
                    }
                    System.out.println("Test Server End");
                    s.close();
                } catch (Exception e) {
                    System.out.println("Test Server Error: " + e.toString());
                }
            }
        }).start();
    }
