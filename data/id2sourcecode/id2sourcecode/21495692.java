    public String save() throws URISyntaxException, IOException, DocumentException {
        if (isInstalled()) {
            return ajaxJsonErrorMessage("SHOP++已完成安装，请勿重复操作！");
        }
        if (StringUtils.isEmpty(databaseHost)) {
            return ajaxJsonErrorMessage("请填写数据库主机!");
        }
        if (StringUtils.isEmpty(databasePort)) {
            return ajaxJsonErrorMessage("请填写数据库端口!");
        }
        if (StringUtils.isEmpty(databaseUsername)) {
            return ajaxJsonErrorMessage("请填写数据库用户名!");
        }
        if (StringUtils.isEmpty(databasePassword)) {
            return ajaxJsonErrorMessage("请填写数据库密码!");
        }
        if (StringUtils.isEmpty(databaseName)) {
            return ajaxJsonErrorMessage("请填写数据库名称!");
        }
        if (StringUtils.isEmpty(adminUsername)) {
            return ajaxJsonErrorMessage("请填写管理员用户名!");
        }
        if (StringUtils.isEmpty(adminPassword)) {
            return ajaxJsonErrorMessage("请填写管理员密码!");
        }
        if (StringUtils.isEmpty(installStatus)) {
            Map<String, String> jsonMap = new HashMap<String, String>();
            jsonMap.put(STATUS, "requiredCheckFinish");
            return ajaxJson(jsonMap);
        }
        String jdbcUrl = "jdbc:mysql://" + databaseHost + ":" + databasePort + "/" + databaseName + "?useUnicode=true&characterEncoding=UTF-8";
        Connection connection = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        try {
            connection = DriverManager.getConnection(jdbcUrl, databaseUsername, databasePassword);
            DatabaseMetaData databaseMetaData = connection.getMetaData();
            String[] types = { "TABLE" };
            resultSet = databaseMetaData.getTables(null, databaseName, "%", types);
            if (StringUtils.equalsIgnoreCase(installStatus, "databaseCheck")) {
                Map<String, String> jsonMap = new HashMap<String, String>();
                jsonMap.put(STATUS, "databaseCheckFinish");
                return ajaxJson(jsonMap);
            }
            if (StringUtils.equalsIgnoreCase(installStatus, "databaseCreate")) {
                StringBuffer stringBuffer = new StringBuffer();
                BufferedReader bufferedReader = null;
                String sqlFilePath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + SQL_INSTALL_FILE_NAME;
                bufferedReader = new BufferedReader(new InputStreamReader(new FileInputStream(sqlFilePath), "UTF-8"));
                String line = "";
                while (null != line) {
                    line = bufferedReader.readLine();
                    stringBuffer.append(line);
                    if (null != line && line.endsWith(";")) {
                        System.out.println("[SHOP++安装程序]SQL: " + line);
                        preparedStatement = connection.prepareStatement(stringBuffer.toString());
                        preparedStatement.executeUpdate();
                        stringBuffer = new StringBuffer();
                    }
                }
                String insertAdminSql = "INSERT INTO `admin` VALUES ('402881862bec2a21012bec2bd8de0003','2010-10-10 0:0:0','2010-10-10 0:0:0','技术部','admin@nodeshop.net',b'1',b'0',b'0',b'0',NULL,NULL,0,NULL,'管理员','" + DigestUtils.md5Hex(adminPassword) + "','" + adminUsername + "');";
                String insertAdminRoleSql = "INSERT INTO `admin_role` VALUES ('402881862bec2a21012bec2bd8de0003','402881862bec2a21012bec2b70510002');";
                preparedStatement = connection.prepareStatement(insertAdminSql);
                preparedStatement.executeUpdate();
                preparedStatement = connection.prepareStatement(insertAdminRoleSql);
                preparedStatement.executeUpdate();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return ajaxJsonErrorMessage("数据库连接失败，请检查数据库设置信息!");
        } finally {
            try {
                if (resultSet != null) {
                    resultSet.close();
                    resultSet = null;
                }
                if (preparedStatement != null) {
                    preparedStatement.close();
                    preparedStatement = null;
                }
                if (connection != null) {
                    connection.close();
                    connection = null;
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        String configFilePath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + JDBC_CONFIG_FILE_NAME;
        Properties properties = new Properties();
        properties.put("jdbc.driver", "com.mysql.jdbc.Driver");
        properties.put("jdbc.url", jdbcUrl);
        properties.put("jdbc.username", databaseUsername);
        properties.put("jdbc.password", databasePassword);
        properties.put("hibernate.dialect", "org.hibernate.dialect.MySQLDialect");
        properties.put("hibernate.show_sql", "false");
        properties.put("hibernate.format_sql", "false");
        OutputStream outputStream = new FileOutputStream(configFilePath);
        properties.store(outputStream, JDBC_CONFIG_FILE_DESCRIPTION);
        outputStream.close();
        String backupWebConfigFilePath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + BACKUP_WEB_CONFIG_FILE_NAME;
        String backupApplicationContextConfigFilePath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + BACKUP_APPLICATION_CONTEXT_CONFIG_FILE_NAME;
        String backupCompassApplicationContextConfigFilePath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + BACKUP_COMPASS_APPLICATION_CONTEXT_CONFIG_FILE_NAME;
        String backupSecurityApplicationContextConfigFilePath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + BACKUP_SECURITY_APPLICATION_CONTEXT_CONFIG_FILE_NAME;
        String webConfigFilePath = new File(Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath()).getParent() + "/" + WEB_CONFIG_FILE_NAME;
        String applicationContextConfigFilePath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + APPLICATION_CONTEXT_CONFIG_FILE_NAME;
        String compassApplicationContextConfigFilePath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + COMPASS_APPLICATION_CONTEXT_CONFIG_FILE_NAME;
        String securityApplicationContextConfigFilePath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + SECURITY_APPLICATION_CONTEXT_CONFIG_FILE_NAME;
        FileUtils.copyFile(new File(backupWebConfigFilePath), new File(webConfigFilePath));
        FileUtils.copyFile(new File(backupApplicationContextConfigFilePath), new File(applicationContextConfigFilePath));
        FileUtils.copyFile(new File(backupCompassApplicationContextConfigFilePath), new File(compassApplicationContextConfigFilePath));
        FileUtils.copyFile(new File(backupSecurityApplicationContextConfigFilePath), new File(securityApplicationContextConfigFilePath));
        String systemConfigFilePath = Thread.currentThread().getContextClassLoader().getResource("").toURI().getPath() + SystemConfigUtil.CONFIG_FILE_NAME;
        File systemConfigFile = new File(systemConfigFilePath);
        SAXReader saxReader = new SAXReader();
        Document document = saxReader.read(systemConfigFile);
        Element rootElement = document.getRootElement();
        Element systemConfigElement = rootElement.element("systemConfig");
        Node isInstalledNode = document.selectSingleNode("/nodeshop/systemConfig/isInstalled");
        if (isInstalledNode == null) {
            isInstalledNode = systemConfigElement.addElement("isInstalled");
        }
        isInstalledNode.setText("true");
        try {
            OutputFormat outputFormat = OutputFormat.createPrettyPrint();
            outputFormat.setEncoding("UTF-8");
            outputFormat.setIndent(true);
            outputFormat.setIndent("	");
            outputFormat.setNewlines(true);
            XMLWriter xmlWriter = new XMLWriter(new FileOutputStream(systemConfigFile), outputFormat);
            xmlWriter.write(document);
            xmlWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return ajaxJsonSuccessMessage("NODESHOP安装成功，请重新启动服务器！");
    }
