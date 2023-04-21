    public TelnetSqlitePreparedStatement(Socket socket, Reader reader, Writer writer, String sql) {
        super();
        this.socket = socket;
        this.reader = reader;
        this.writer = writer;
        this.sql = sql;
        this.closed = false;
    }
