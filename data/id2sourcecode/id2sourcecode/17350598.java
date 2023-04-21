    public Configuration(String configurationFilename) throws IOException, ClassNotFoundException, IllegalAccessException, InstantiationException {
        this.configurationFilename = configurationFilename;
        FileInputStream fis = new FileInputStream(new File(configurationFilename));
        Properties prop = new Properties();
        prop.load(fis);
        String protocol = prop.getProperty("server.protocol", "mysql").trim().toLowerCase();
        if ("mysql".equals(protocol)) {
            serverProtocol = MYSQL_PROTOCOL_TYPE;
        } else if ("postgresql".equals(protocol)) {
            serverProtocol = POSTGRESQL_PROTOCOL_TYPE;
        }
        serverVersion = prop.getProperty("server.version", "4.1.1-myosotis-0.0.0").trim();
        serverPort = Integer.parseInt(prop.getProperty("server.port", "9999").trim());
        setListenAddress(prop.getProperty("server.listen.address", null));
        jdbcDriverBaseURL = prop.getProperty("jdbc.driver.base.url", "jdbc:sequoia://").trim();
        setJdbcDriverOptions(prop.getProperty("jdbc.driver.options", "").trim());
        connectionCloseIdleTimeout = Integer.parseInt(prop.getProperty("connection.close.idle.timeout", "0").trim());
        fetchSize = Integer.parseInt(prop.getProperty("statement.fetch.size", "0").trim());
        timestampFormat = prop.getProperty("timestamp.format", "yyyy-MM-dd HH:mm:ss").trim();
        if ("".equals(timestampFormat)) timestampFormat = "yyyy-MM-dd HH:mm:ss";
        mirrorZeroDateTimeBehavior = prop.getProperty("mirrorZeroDateTimeBehavior", "no");
        forcedDBforUnspecConnections = prop.getProperty("forcedDBforUnspecConnections", FORCE_DB_DEFAULT).trim();
        ignoreSQLComments = Boolean.parseBoolean(prop.getProperty("ignoreSQLComments", "true").trim());
        manageTransactionsLocally = Boolean.parseBoolean(prop.getProperty("manageTransactionsLocally", "false").trim());
        maxConsecutiveClientConnectionFailures = Integer.parseInt(prop.getProperty("maxConsecutiveClientConnectionFailures", "10"));
        setPassThroughMode(Boolean.parseBoolean(prop.getProperty("passThroughMode", "true").trim()));
        String jdbcDriverClassname = prop.getProperty("jdbc.driver", "org.continuent.sequoia.driver.Driver").trim();
        try {
            driver = (Driver) Class.forName(jdbcDriverClassname).newInstance();
        } catch (ClassNotFoundException e) {
            logger.error("Could not find class " + jdbcDriverClassname + ". Make sure the appropriate driver is in the lib/ directory");
            throw e;
        }
        this.jdbcDirectDriverClassname = prop.getProperty("jdbc.direct-connection.driver", "").trim();
        if (!"".equals(this.jdbcDirectDriverClassname)) {
            try {
                Class.forName(jdbcDirectDriverClassname);
            } catch (ClassNotFoundException e) {
                logger.error("Could not find class " + jdbcDirectDriverClassname + ". Make sure the appropriate driver is in the lib/ directory");
                throw e;
            }
        }
        String userMapFilename = prop.getProperty("user.map.filename", "../conf/user.map").trim();
        userMap = new UserMap(userMapFilename);
        userMap.readConfig();
        authorizedHostsFileName = prop.getProperty("authorized.hosts.file", "../conf/authorized_hosts");
        BufferedReader input = null;
        try {
            input = new BufferedReader(new FileReader(authorizedHostsFileName));
            authorizedHosts = new ArrayList<String>();
            String line = null;
            while ((line = input.readLine()) != null) {
                line = line.trim();
                if (!line.startsWith("#")) authorizedHosts.add(line);
            }
        } catch (IOException e) {
            logger.warn("No or unreadable authorized hosts file " + authorizedHostsFileName + " - NOT using IP authentication");
            authorizedHosts = null;
        } finally {
            if (input != null) try {
                input.close();
            } catch (IOException ignored) {
            }
        }
        tungstenCommandBeginMarker = prop.getProperty("tungsten.command.begin.marker", tungstenCommandBeginMarker).trim();
        tungstenCommandEndMarker = prop.getProperty("tungsten.command.end.marker", tungstenCommandEndMarker).trim();
        selectiveRwSplittingMarker = prop.getProperty("selective.rwsplitting.marker", selectiveRwSplittingMarker).trim();
        selectiveRwSplitting = Boolean.valueOf(prop.getProperty("selective.rwsplitting", "false"));
        if (selectiveRwSplitting == true) {
            logger.info(String.format("Using SELECTIVE read/write splitting, using marker=%s", selectiveRwSplittingMarker));
        }
        setUseSmartScale(Boolean.valueOf(prop.getProperty("useSmartScale", "false")));
        setAutoReconnect(Boolean.valueOf(prop.getProperty("autoReconnect", "true")));
    }
