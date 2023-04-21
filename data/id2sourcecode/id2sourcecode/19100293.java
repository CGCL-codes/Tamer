    public Connection getConnection(String webPath, String dataType, String dataSource, String dataPort, String dataBase, String db_user, String db_pwd) {
        String db_driver = "", db_url = "";
        try {
            String dbConfig = webPath + "WEB-INF/database/" + dataType + "/dbconfig.properties";
            db_driver = PropertiesUtil.getStringValue(dbConfig, GlobalsKeys.DATABASE_DRIVER);
            db_url = PropertiesUtil.getStringValue(dbConfig, GlobalsKeys.DATABASE_URL);
            db_url = db_url.replaceFirst("DB_HOST", dataSource);
            db_url = db_url.replaceFirst("DB_PORT", dataPort);
            db_url = db_url.replaceFirst("DB_NAME", dataBase);
            if (logger.isDebugEnabled()) {
                logger.debug("当前驱动：" + db_driver + ",连接字符串：" + db_url + ",用户名：" + db_user + ",密码：" + db_pwd);
            }
            Class.forName(db_driver);
            Connection connection = DriverManager.getConnection(db_url, db_user, db_pwd);
            String db_dialect = PropertiesUtil.getStringValue(dbConfig, GlobalsKeys.DATABASE_DIALECT);
            String appxml = webPath + "WEB-INF/config/applicationContext.xml";
            String configStr = FileUtils.readFileToString(new File(appxml), "UTF-8");
            configStr = configStr.replaceFirst("DB_DRIVER", db_driver);
            configStr = configStr.replaceFirst("DB_URL", db_url);
            configStr = configStr.replaceFirst("DB_USER", db_user);
            configStr = configStr.replaceFirst("DB_PWD", db_pwd);
            configStr = configStr.replaceFirst("DB_DIALECT", db_dialect);
            String newFile = webPath + "WEB-INF/classes/applicationContext.xml";
            FileUtils.writeStringToFile(new File(newFile), configStr, "UTF-8");
            FileUtils.copyFile(new File(webPath + "WEB-INF/config/web.xml"), new File(webPath + "WEB-INF/web.xml"));
            FileUtils.copyFile(new File(webPath + "WEB-INF/config/index.html"), new File(webPath + "index.html"));
            return connection;
        } catch (ClassNotFoundException e) {
            logger.error("加载驱动：" + db_driver + "异常", e);
            throw new ServiceException("找不到相应的连接驱动", e);
        } catch (SQLException e) {
            logger.error("连接数据库异常：" + db_url, e);
            throw new ServiceException("数据库连接失败," + e.getMessage(), e);
        } catch (Exception e) {
            logger.error("不可能发生的错误", e);
            throw new ServiceException("不可能发生的错误," + e.getMessage(), e);
        }
    }
