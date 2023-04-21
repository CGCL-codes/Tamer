    public void connect() throws ExtractorException {
        try {
            logger.info("Connecting to master MySQL server: url=" + url);
            Class.forName("org.drizzle.jdbc.DrizzleDriver");
            conn = DriverManager.getConnection(url, login, password);
        } catch (ClassNotFoundException e) {
            throw new ExtractorException("Unable to load JDBC driver", e);
        } catch (SQLException e) {
            throw new ExtractorException("Unable to connect", e);
        }
        try {
            MySQLIOs io = MySQLIOs.getMySQLIOs(conn);
            input = io.getInput();
            output = io.getOutput();
        } catch (Exception e) {
            throw new ExtractorException("Unable to access IO streams for connection", e);
        }
        this.relayDir = new File(binlogDir);
        if (!relayDir.isDirectory()) throw new ExtractorException("Relay log directory not a directory or does not exist: " + relayDir.getAbsolutePath()); else if (!relayDir.canWrite()) throw new ExtractorException("Relay log directory is not writable: " + relayDir.getAbsolutePath());
        binlogIndex = new File(relayDir, binlogPrefix + ".index");
        if (autoClean) {
            if (binlogIndex.delete()) logger.info("Cleaned up binlog index file: " + binlogIndex.getAbsolutePath());
            String baseLog;
            if (this.binlog == null) baseLog = ""; else baseLog = binlog;
            for (File child : relayDir.listFiles()) {
                if (!child.isFile()) continue; else if (!child.getName().startsWith(this.binlogPrefix)) continue; else if (child.getName().compareTo(baseLog) < 0) continue;
                if (child.delete()) logger.info("Cleaned up binlog file: " + child.getAbsolutePath());
            }
        }
        try {
            logger.info("Requesting binlog data from master: " + binlog + ":" + offset);
            sendBinlogDumpPacket(output);
        } catch (IOException e) {
            throw new ExtractorException("Error sending request to dump binlog", e);
        }
    }
