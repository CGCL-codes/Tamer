    private void processConnection(ConnectionEvent event, HashMap list) {
        try {
            Socket tx_socket = event.getSocket();
            tx_socket.setKeepAlive(true);
            tx_socket.setTcpNoDelay(true);
            int code = tx_socket.hashCode();
            BufferedWriter tx_writer = new BufferedWriter(new OutputStreamWriter(tx_socket.getOutputStream()));
            AceInputSocketStream reader_thread = new AceInputSocketStream(code, "AceLogger", tx_socket, true);
            reader_thread.start();
            list.put(new Integer(code), new LogProcessor.SocketInfo(tx_writer, reader_thread));
        } catch (Exception ex) {
            log(AceLogger.WARNING, AceLogger.SYSTEM_LOG, "LogProcessor.processConnection() -- Error creating socket stream : " + ex.getClass().getName() + ": " + ex.getMessage());
        }
    }
