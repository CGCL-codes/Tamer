    private void generateObjectiveFile(String objdataFile, String path, String pureMapName) {
        String objdataFileDestination = path + MAPS_FOLDER + pureMapName + OBJECTIVEDATA_FILE_EXTENSION;
        String outcome = FileUtils.copyFile(objdataFile, objdataFileDestination);
        if (outcome != null) {
            System.err.println(outcome);
        }
    }
