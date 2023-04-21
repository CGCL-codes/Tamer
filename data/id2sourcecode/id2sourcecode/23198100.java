    public static void main(String[] args) throws Exception {
        PropertyConfigurator.configure("log4j.properties");
        String hostName = "localhost";
        String dbName = "unittest_lgd";
        String userName = "postgres";
        String passWord = "postgres";
        String osmosisPath = "/opt/osmosis/0.35.1/bin/osmosis";
        String zippedFile = "data/test/osm/bremen.osm.bz2";
        File unzipped = extract(new File(zippedFile));
        String dataFile = unzipped.getCanonicalPath();
        dropUnitTestDB(hostName, dbName, "postgres", "postgres");
        createUnitTestDB(hostName, dbName, userName, passWord);
        loadUnitTestDB(hostName, dbName, userName, passWord);
        logger.info("Loading dataset using osmosis");
        String[] options = { "--read-xml", "file=" + dataFile, "--write-pgsql", "host=" + hostName, "database=" + dbName, "user=" + userName, "password=" + passWord };
        Runtime runtime = Runtime.getRuntime();
        String command = osmosisPath + " " + org.linkedgeodata.util.StringUtil.implode(" ", options);
        logger.debug("Executing system call: " + command);
        Process p = runtime.exec(command);
        logger.debug("Waiting for process to terminate...");
        p.waitFor();
        InputStream in = p.getInputStream();
        logger.trace("Console output of process:\n" + StreamUtil.toString(in));
        in.close();
        logger.debug("Process terminated with exit code '" + p.exitValue() + "'");
        logger.info("Performing post-load actions");
        postLoadUnitTestDB(hostName, dbName, userName, passWord);
    }
