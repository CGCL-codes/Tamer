        public void run() {
            while (isRunning) {
                try {
                    Socket s = serverSocket.getChannel().socket().accept();
                    handle(s);
                } catch (Exception e) {
                }
            }
            try {
                serverSocket.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
