    public void setServerConnection(Socket s, BufferedReader reader, BufferedWriter writer) {
        Connection tmp = new Connection(s, null, reader, writer);
        this.serverConnection = tmp;
    }
