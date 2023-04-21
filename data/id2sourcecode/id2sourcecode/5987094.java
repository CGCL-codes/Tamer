    public void adjustScience(int acres, Race race) {
        science.setAlchemy(getRaw(acres, race, science.getAlchemy(), Science.ALCHEMY_FACTOR));
        science.setTools(getRaw(acres, race, science.getTools(), Science.TOOLS_FACTOR));
        science.setHousing(getRaw(acres, race, science.getHousing(), Science.HOUSING_FACTOR));
        science.setFood(getRaw(acres, race, science.getFood(), Science.FOOD_FACTOR));
        science.setMilitary(getRaw(acres, race, science.getMilitary(), Science.MILITARY_FACTOR));
        science.setCrime(getRaw(acres, race, science.getCrime(), Science.CRIME_FACTOR));
        science.setChanneling(getRaw(acres, race, science.getChanneling(), Science.CHANNELING_FACTOR));
    }
