        public void run() {
            Socket socket = null;
            ObjectInputStream in;
            while (isRunning) {
                try {
                    socket = serverSocket.accept();
                    in = new ObjectInputStream(socket.getInputStream());
                    deliver((Message) in.readObject());
                } catch (IOException e) {
                    writeDebug("IOException while trying to accept connection!", true);
                    e.printStackTrace();
                } catch (ClassNotFoundException e) {
                    writeDebug("ClassNotFoundException while trying to read Object!", true);
                } finally {
                    try {
                        if (socket != null) socket.close();
                    } catch (Exception e) {
                    }
                }
            }
            try {
                if (serverSocket != null) serverSocket.close();
            } catch (Exception e) {
            }
        }
