    private void saveProvinceDetails(Province province, boolean updateMode) throws SQLException {
        Military military = province.getMilitary();
        Science science = province.getScience();
        Survey survey = province.getSurvey();
        String militaryRaw = "N";
        if (military.isRaw()) {
            militaryRaw = "Y";
        }
        if (updateMode) {
            QueryHandler.update(connection, SQL.getProperty("update.science"), science.getLastUpdate(), science.getAlchemy(), science.getTools(), science.getHousing(), science.getFood(), science.getMilitary(), science.getCrime(), science.getChanneling(), science.getProvince().getId());
            QueryHandler.update(connection, SQL.getProperty("update.military"), military.getLastUpdate(), military.getOffense(), military.getDefense(), militaryRaw, military.getProvince().getId());
            QueryHandler.update(connection, SQL.getProperty("update.survey"), survey.getLastUpdate(), survey.getEfficiency(), survey.getBarren(), survey.getHomes(), survey.getFarms(), survey.getMills(), survey.getBanks(), survey.getTrainingGrounds(), survey.getBarracks(), survey.getArmories(), survey.getForts(), survey.getGuardStations(), survey.getHospitals(), survey.getGuilds(), survey.getTowers(), survey.getThievesDens(), survey.getWatchtowers(), survey.getLibraries(), survey.getSchools(), survey.getStables(), survey.getDungeons(), survey.getProvince().getId());
        } else {
            QueryHandler.insert(connection, SQL.getProperty("insert.science"), science.getId(), science.getProvince().getId(), science.getLastUpdate(), science.getAlchemy(), science.getTools(), science.getHousing(), science.getFood(), science.getMilitary(), science.getCrime(), science.getChanneling());
            QueryHandler.insert(connection, SQL.getProperty("insert.military"), military.getId(), military.getProvince().getId(), military.getLastUpdate(), military.getOffense(), military.getDefense(), militaryRaw);
            QueryHandler.insert(connection, SQL.getProperty("insert.survey"), survey.getId(), survey.getProvince().getId(), survey.getLastUpdate(), survey.getEfficiency(), survey.getBarren(), survey.getHomes(), survey.getFarms(), survey.getMills(), survey.getBanks(), survey.getTrainingGrounds(), survey.getBarracks(), survey.getArmories(), survey.getForts(), survey.getGuardStations(), survey.getHospitals(), survey.getGuilds(), survey.getTowers(), survey.getThievesDens(), survey.getWatchtowers(), survey.getLibraries(), survey.getSchools(), survey.getStables(), survey.getDungeons());
        }
        QueryHandler.delete(connection, SQL.getProperty("delete.army.by.militaryId"), military.getId());
        for (Army army : military.getArmies()) {
            QueryHandler.insert(connection, SQL.getProperty("insert.army"), army.getId(), army.getMilitary().getId(), army.getGenerals(), army.getSoldiers(), army.getOffspecs(), army.getDefspecs(), army.getElites(), army.getHorses(), army.getSpoils(), army.getReturnTime());
        }
    }
