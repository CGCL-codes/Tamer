    private void beginCustomerSession(Socket socket) throws IOException {
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        NetworkBankingSession session = new NetworkBankingSession(reader, writer);
        Thread sessionThread = new Thread(session);
        sessionThread.start();
    }
