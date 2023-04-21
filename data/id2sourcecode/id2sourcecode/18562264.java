    public static void main(String[] args) {
        if (args.length < 1) {
            debug("Syntax: java -jar nalasys.jar language=<lang-code> database=<jdbc-url> server=<server-port> src=<filename-of-text-to-interpret>");
            return;
        }
        Map params = Types.toMap(args);
        params.put("input", new Stream(new InputStreamReader(System.in)));
        params.put("output", new Stream(System.out));
        Context.init(params);
        TcpServer server = null;
        Object port = params.get("server");
        if (port != null && !port.toString().trim().equals("")) {
            try {
                server = new TcpServer(Integer.parseInt(port.toString().trim()));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Object src = params.get("src");
        if (src == null) interactive("\n? "); else {
            try {
                write(Types.toString(parse(read(src), null)) + "\n", null);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (server != null) server.close();
    }
