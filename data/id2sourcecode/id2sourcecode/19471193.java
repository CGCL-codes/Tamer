    public Connection(Socket s, Contact c, BufferedReader reader, BufferedWriter writer) {
        this.s = s;
        this.c = c;
        this.reader = reader;
        this.writer = writer;
    }
