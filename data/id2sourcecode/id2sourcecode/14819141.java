    public String getClientJsonStrFromMemCache(String uuid) {
        ng = new NameGenerator("languages.txt", "semantics.txt");
        String[] namegn = ng.getRandomName("Finnish", "Male", 1);
        uuidcr = UUID.randomUUID();
        now = new Date();
        ABeanFactory factory = AutoBeanFactorySource.create(ABeanFactory.class);
        AutoBeanHandlerImpl autoBeanHandlerImpl = new AutoBeanHandlerImpl();
        CliCookie cliCookie = autoBeanHandlerImpl.makeCliCookie(factory);
        cliCookie.setId(uuidcr.toString());
        cliCookie.setCity("Helsinki");
        cliCookie.setCreated_at(now);
        cliCookie.setUpdated_at(now);
        cliCookie.setNickname(namegn[0]);
        channelKey = null;
        if (channelKey == null) {
            channelKey = ChannelServiceFactory.getChannelService().createChannel(uuidcr.toString());
            log.info("channelKey created " + channelKey);
        } else {
            log.info("channelKey NOT created " + channelKey);
        }
        cliCookie.setToken(channelKey);
        log.info(cliCookie.toString());
        json = autoBeanHandlerImpl.serializeToJsonCliCookie(cliCookie);
        Environment env = ApiProxy.getCurrentEnvironment();
        if (env.getAttributes().get("com.google.appengine.runtime.default_version_hostname") != null) {
            server = env.getAttributes().get("com.google.appengine.runtime.default_version_hostname").toString();
        } else {
            server = "server";
        }
        java.sql.Timestamp nowsql = new java.sql.Timestamp(now.getTime());
        c = null;
        try {
            DriverManager.registerDriver(new AppEngineDriver());
            c = (Connection) DriverManager.getConnection("jdbc:google:rdbms://sinelga.com:sinelgamysql:sinelga/keskustelu");
            statement = "INSERT INTO online (id,nickname,channel,server,created_at,updated_at,status) VALUES(?,?,?,?,?,?,?)";
            PreparedStatement stmt = c.prepareStatement(statement);
            stmt.setString(1, uuidcr.toString());
            stmt.setString(2, namegn[0]);
            stmt.setString(3, channelKey);
            stmt.setString(4, server);
            stmt.setTimestamp(5, nowsql);
            stmt.setTimestamp(6, nowsql);
            stmt.setString(7, "connected");
            int success = 2;
            success = stmt.executeUpdate();
            if (success == 1) {
                log.info("Update Ok");
            } else if (success == 0) {
                log.severe("Update NOT Ok");
            }
        } catch (SQLException e) {
            log.severe(e.getMessage());
        } finally {
            if (c != null) try {
                c.close();
            } catch (SQLException ignore) {
            }
        }
        return json;
    }
