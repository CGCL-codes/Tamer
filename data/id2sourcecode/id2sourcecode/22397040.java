    @Override
    protected TestRunnable createTestServer() {
        return new TestRunnable() {

            public void run() throws Exception {
                LocalServerSocket serverSocket = new LocalServerSocket(new LocalSocketAddress(socketName, socketAbstractNamespace));
                try {
                    System.out.println("server starts");
                    serverAcceptsNotifyAll();
                    Socket socket = serverSocket.accept();
                    InputStream in = socket.getInputStream();
                    OutputStream out = socket.getOutputStream();
                    int len = in.read();
                    out.write(len);
                    for (int i = 0; i < len; i++) {
                        out.write(in.read());
                    }
                    Thread.sleep(500);
                    in.close();
                    out.close();
                    socket.close();
                } finally {
                    serverSocket.close();
                    System.out.println("server ends");
                }
            }
        };
    }
