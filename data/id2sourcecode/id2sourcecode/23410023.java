    public Configuration(ConfigListener progress, int pass, Options options) {
        try {
            URL url = Configuration.class.getResource(SOLIDBASE_DEFAULT_PROPERTIES);
            if (url == null) throw new SystemException(SOLIDBASE_DEFAULT_PROPERTIES + " not found in classpath");
            progress.readingConfigFile(url.toString());
            this.properties = new Properties();
            InputStream input = url.openStream();
            try {
                this.properties.load(input);
            } finally {
                input.close();
            }
            File file;
            if (options.config != null) file = new File(options.config); else file = getPropertiesFile();
            if (file.exists()) {
                progress.readingConfigFile(file.getAbsolutePath());
                this.properties = new Properties(this.properties);
                input = new FileInputStream(file);
                try {
                    this.properties.load(input);
                } finally {
                    input.close();
                }
                String s = this.properties.getProperty("properties-version");
                if (!"1.0".equals(s)) throw new FatalException("Expecting properties-version 1.0 in the properties file");
            }
            Properties commandLineProperties = new Properties(this.properties);
            if (options.driver != null) commandLineProperties.put("connection.driver", options.driver);
            if (options.url != null) commandLineProperties.put("connection.url", options.url);
            if (options.username != null) commandLineProperties.put("connection.username", options.username);
            if (options.password != null) commandLineProperties.put("connection.password", options.password);
            if (options.target != null) commandLineProperties.put("upgrade.target", options.target);
            if (options.upgradefile != null) commandLineProperties.put("upgrade.file", options.upgradefile);
            if (options.sqlfile != null) commandLineProperties.put("sql.file", options.sqlfile);
            if (!commandLineProperties.isEmpty()) this.properties = commandLineProperties;
            this.driverJars = new ArrayList<String>();
            String driversProperty = this.properties.getProperty("classpath.ext");
            if (driversProperty != null) for (String driverJar : driversProperty.split(";")) {
                driverJar = driverJar.trim();
                if (driverJar.length() > 0) this.driverJars.add(driverJar);
            }
            if (pass > 1) {
                String driver = this.properties.getProperty("connection.driver");
                String dbUrl = this.properties.getProperty("connection.url");
                String userName = this.properties.getProperty("connection.username");
                String password = this.properties.getProperty("connection.password");
                String upgradeFile = this.properties.getProperty("upgrade.file");
                String target = this.properties.getProperty("upgrade.target");
                String sqlFile = this.properties.getProperty("sql.file");
                if (driver != null || dbUrl != null || userName != null || password != null) this.defaultDatabase = new Database("default", driver, dbUrl, userName, password);
                this.upgradeFile = upgradeFile;
                this.target = target;
                this.sqlFile = sqlFile;
                for (Entry<Object, Object> entry : this.properties.entrySet()) {
                    String key = (String) entry.getKey();
                    Matcher matcher = propertyPattern.matcher(key);
                    if (matcher.matches()) {
                        String name = matcher.group(1);
                        String prop = matcher.group(2);
                        String value = (String) entry.getValue();
                        Database database = this.secondaryDatabases.get(name);
                        if (database == null) {
                            database = new Database(name);
                            this.secondaryDatabases.put(name, database);
                        }
                        if (prop.equals("driver")) database.setDriver(value); else if (prop.equals("url")) database.setUrl(value); else if (prop.equals("username")) database.setUserName(value); else if (prop.equals("password")) database.setPassword(value); else Assert.fail();
                    }
                }
                for (Database database : this.secondaryDatabases.values()) {
                    if (database.getName().equals("default")) throw new FatalException("The secondary connection name 'default' is not allowed");
                    if (StringUtils.isBlank(database.getUserName())) throw new FatalException("Property 'connection." + database.getName() + ".username' must be specified in " + SOLIDBASE_PROPERTIES);
                }
            }
        } catch (IOException e) {
            throw new SystemException(e);
        }
    }
