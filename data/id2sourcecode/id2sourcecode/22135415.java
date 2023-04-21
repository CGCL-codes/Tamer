    protected void setUp() throws SQLException {
        try {
            Class.forName("org.hsqldb.jdbc.JDBCDriver");
        } catch (Exception e) {
        }
        saCon = DriverManager.getConnection(appDbUrl, "SA", "");
        saCon.setAutoCommit(false);
        saSt = saCon.createStatement();
        saSt.executeUpdate("CREATE ROLE role1");
        saSt.executeUpdate("CREATE ROLE role2");
        saSt.executeUpdate("CREATE ROLE role3");
        saSt.executeUpdate("CREATE ROLE role4");
        saSt.executeUpdate("CREATE SCHEMA s1");
        saSt.executeUpdate("CREATE SCHEMA s2");
        saSt.executeUpdate("CREATE SCHEMA s3");
        saSt.executeUpdate("SET DATABASE AUTHENTICATION FUNCTION EXTERNAL NAME " + "'CLASSPATH:" + "org.hsqldb.auth.AuthBeanMultiplexer.authenticate'");
        plexer = AuthBeanMultiplexer.getSingleton();
        plexer.clear();
        if (jaasCfgFile == null) {
            int i;
            byte[] copyBuffer = new byte[512];
            InputStream iStream = null;
            OutputStream oStream = null;
            try {
                iStream = getClass().getResourceAsStream(cfgResourcePath);
                if (iStream == null) throw new IOException("Failed to read resource: " + cfgResourcePath);
                jaasCfgFile = File.createTempFile(getClass().getName().replaceFirst(".*\\.", ""), ".jaascfg");
                jaasCfgFile.deleteOnExit();
                oStream = new FileOutputStream(jaasCfgFile);
                while ((i = iStream.read(copyBuffer)) > -1) oStream.write(copyBuffer, 0, i);
            } catch (IOException ioe) {
                logger.severe("Failed to prepare JAAS config file in local " + "file system", ioe);
                throw new IllegalStateException("Failed to prepare JAAS " + "config file in local file system", ioe);
            } finally {
                try {
                    if (oStream != null) {
                        oStream.close();
                        oStream = null;
                    }
                    if (iStream != null) {
                        iStream.close();
                        iStream = null;
                    }
                } catch (IOException ioe) {
                    logger.error("Failed to clear file objects");
                }
            }
        }
        savedLoginConfig = System.getProperty("java.security.auth.login.config");
        System.setProperty("java.security.auth.login.config", jaasCfgFile.getAbsolutePath());
    }
