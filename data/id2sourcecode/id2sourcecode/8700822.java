    public void addConnection(Socket s, Contact c, BufferedReader reader, BufferedWriter writer) {
        Connection connection = new Connection(s, c, reader, writer);
        cl.add(connection);
    }
