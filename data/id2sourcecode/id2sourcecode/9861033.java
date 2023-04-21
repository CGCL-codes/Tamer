    public EventsToDB(String networkFile, String facilitiesFile, String populationFile, String eventsFile, String password) throws SQLException {
        dbConnectionTool = new DBConnectionTool();
        log.info("loading scenario...");
        loadScenario(networkFile, facilitiesFile, populationFile);
        log.info("done.");
        log.info("getting home facilities...");
        getHomeFacilities();
        log.info("done.");
        connectToDB(password);
        log.info("writing home facilities events to the DB...");
        writeHomeFacilitiesToDB();
        log.info("done.");
        log.info("reading events from file and write them to the DB...");
        readEventsFile(eventsFile);
        log.info("done.");
        disconnectFromDB();
    }
