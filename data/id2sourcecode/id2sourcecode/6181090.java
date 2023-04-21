    public Deployer(String[] args) throws Exception {
        defaultConnection = false;
        String database = null;
        String user = null;
        String password = null;
        for (int i = 0; i < args.length; ++i) {
            if (args[i] == null) continue;
            boolean moreArgs = (i + 1 < args.length && !args[i + 1].startsWith("-"));
            if (args[i].equals("--database")) {
                if (moreArgs) database = args[i + 1];
            } else if (args[i].equals("--user")) {
                if (moreArgs) user = args[i + 1];
            } else if (args[i].equals("--password")) {
                if (moreArgs) password = args[i + 1];
            } else continue;
            if (moreArgs) {
                args[i] = args[i + 1] = null;
                ++i;
            } else throw new Exception("Missing argument for option " + args[i]);
        }
        if (database == null) throw new Exception("Option --database must be present");
        conn = DriverManager.getConnection("jdbc:firebirdsql:" + database, user, password);
        conn.setAutoCommit(false);
    }
