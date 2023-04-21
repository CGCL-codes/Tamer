    public void openReadWrite() {
        try {
            if (socket != null) {
                if (sockOut == null) sockOut = new DataOutputStream(socket.getOutputStream());
                if (sockIn == null) sockIn = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            } else {
                System.err.println("Socket not connected!");
                System.err.println("Socket must be open before read/write");
            }
        } catch (IOException e) {
            System.err.println("Could not open sockets for read/write");
        }
    }
