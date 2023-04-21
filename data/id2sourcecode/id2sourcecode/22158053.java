    protected JFTPStream(BufferedReader reader, BufferedInputStream in, PrintWriter writer, BufferedOutputStream out, GenericCommunication comm) {
        super("org.nilisoft.jftp4i.conn.JFTPStream");
        this._in = in;
        this._reader = reader;
        this._writer = writer;
        this._out = out;
        this.comm = comm;
    }
