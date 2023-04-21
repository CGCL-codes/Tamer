    public String listen() {
        if (my_serverSocket != null) {
            while (true) {
                try {
                    Socket socket = my_serverSocket.accept();
                    System.out.println("Incomming Connection Request: " + socket.getInetAddress());
                    BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    socket.getChannel();
                    String msg = in.readLine();
                    in.close();
                    socket.close();
                    return msg;
                } catch (IOException ioe) {
                    System.out.println("TCP Server is unable to listen...");
                } catch (SecurityException se) {
                    se.printStackTrace();
                }
            }
        } else {
            return null;
        }
    }
