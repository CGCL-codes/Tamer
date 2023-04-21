    private void setSocket(Socket socket) {
        this.socket = socket;
        if (socket != null) {
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream(), true);
            } catch (IOException ioe) {
                ioe.printStackTrace();
                println("Socket reader/writer not instantiated");
                throw new Error();
            }
        }
    }
