    private boolean submitDatabaseInformation(PrintWriter body, CMPMappingDataObject dataObject, String handleFile) throws IOException {
        DatabaseData databaseData = new DatabaseData();
        databaseData.setDbEngine(request.getFormParameter(CMPMappingWriter.FORM_FIELD_DB_ENGINE));
        databaseData.setDriverClass(request.getFormParameter(CMPMappingWriter.FORM_FIELD_DRIVER_CLASS));
        databaseData.setDriverUrl(request.getFormParameter(CMPMappingWriter.FORM_FIELD_DRIVER_URL));
        databaseData.setFileName(request.getFormParameter(CMPMappingWriter.FORM_FIELD_FILE_NAME));
        databaseData.setJndiName(request.getFormParameter(CMPMappingWriter.FORM_FIELD_JNDI_NAME));
        databaseData.setPassword(request.getFormParameter(CMPMappingWriter.FORM_FIELD_PASSWORD));
        databaseData.setUsername(request.getFormParameter(CMPMappingWriter.FORM_FIELD_USERNAME));
        try {
            databaseData.validate();
        } catch (ValidationException e) {
            CMPMappingWriter.printDBInfo(body, e.getMessage(), databaseData, handleFile);
            return false;
        }
        String path = FileUtils.getBase().getDirectory("conf").getAbsolutePath() + System.getProperty("file.separator") + databaseData.getFileName();
        String localDBFileName = path + ".cmp_local_database.xml";
        String globalDBFileName = path + ".cmp_global_database.xml";
        String mappingFileName = path + ".cmp_or_mapping.xml";
        Database globalDatabase = new Database();
        Database localDatabase = new Database();
        globalDatabase.setName(EnvProps.GLOBAL_TX_DATABASE);
        globalDatabase.setEngine(databaseData.getDbEngine());
        localDatabase.setName(EnvProps.LOCAL_TX_DATABASE);
        localDatabase.setEngine(databaseData.getDbEngine());
        Mapping mapping = new Mapping();
        mapping.setHref(mappingFileName);
        globalDatabase.addMapping(mapping);
        localDatabase.addMapping(mapping);
        Jndi jndi = new Jndi();
        jndi.setName(databaseData.getJndiName());
        globalDatabase.setJndi(jndi);
        Driver driver = new Driver();
        Param userNameParam = new Param();
        Param passwordParam = new Param();
        userNameParam.setName("user");
        userNameParam.setValue(databaseData.getUsername());
        passwordParam.setName("password");
        passwordParam.setValue(databaseData.getPassword());
        driver.setClassName(databaseData.getDriverClass());
        driver.setUrl(databaseData.getDriverUrl());
        driver.addParam(userNameParam);
        driver.addParam(passwordParam);
        localDatabase.setDriver(driver);
        try {
            localDatabase.validate();
            globalDatabase.validate();
        } catch (ValidationException e) {
            CMPMappingWriter.printDBInfo(body, e.getMessage(), databaseData, handleFile);
            return false;
        }
        File jdbcDriverSource = new File(request.getFormParameter(CMPMappingWriter.FORM_FIELD_JDBC_DRIVER));
        String libDir = FileUtils.getBase().getDirectory("lib").getAbsolutePath() + System.getProperty("file.separator") + jdbcDriverSource.getName();
        File destFile = new File(libDir);
        if (jdbcDriverSource.isFile()) {
            if (!destFile.exists() && !destFile.createNewFile()) {
                throw new IOException("Could not create file: " + libDir);
            }
            FileUtils.copyFile(destFile, jdbcDriverSource);
        }
        dataObject.setGlobalDatabase(globalDatabase);
        dataObject.setGlobalDatabaseFileName(globalDBFileName);
        dataObject.setLocalDatabase(localDatabase);
        dataObject.setLocalDatabaseFileName(localDBFileName);
        dataObject.setMappingRootFileName(mappingFileName);
        return true;
    }
