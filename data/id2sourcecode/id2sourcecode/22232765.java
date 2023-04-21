    public void createDatabase(String databaseName, boolean overwriteIfExists, boolean checkExisting, boolean omitErrorIfExisting) {
        if (databaseName == null || "".equals(databaseName)) {
            throw new IllegalArgumentException("passed param 'databaseName' must not be null or empty");
        }
        if (checkExisting) {
            if (existsDatabase(databaseName)) {
                if (overwriteIfExists) {
                    deleteDatabase(databaseName);
                } else {
                    if (omitErrorIfExisting) {
                        logger.warn("the database '" + databaseName + "' already exists, could not re-create database (since overwrite=" + overwriteIfExists + " was passed), using OLD existing database");
                        return;
                    } else {
                        throw new IllegalArgumentException("the database '" + databaseName + "' already exists, could not re-create database (since overwrite=" + overwriteIfExists + " was passed)");
                    }
                }
            }
        }
        StringBuilder ddl = new StringBuilder("CREATE DATABASE ");
        ddl.append(databaseName);
        getJdbcTemplate().execute(ddl.toString());
        if (logger.isDebugEnabled()) {
            logger.debug("database '" + databaseName + "' has been created successfully");
            logger.debug("used following statement: " + ddl);
        }
    }
