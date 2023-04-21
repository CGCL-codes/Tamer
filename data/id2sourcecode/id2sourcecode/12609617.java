        private void setSocket(Socket socket) {
            this.socket = socket;
            if (socket != null) {
                try {
                    reader = new DataInputStream(socket.getInputStream());
                    writer = new PrintWriter(socket.getOutputStream(), true);
                    decoder.getRecognizer().getFrontEnd().setDataSource(new SocketDataSource(socket));
                } catch (IOException ioe) {
                    ioe.printStackTrace();
                    System.out.println("Socket reader/writer not instantiated");
                    throw new Error();
                }
            }
        }
