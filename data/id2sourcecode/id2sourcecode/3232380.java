    private void readSingleDataFile(Task task, DataReader reader, File dataFile) {
        if (!dataFile.exists()) {
            _log.error("Could not find data file " + dataFile.getAbsolutePath());
        } else if (!dataFile.isFile()) {
            _log.error("Path " + dataFile.getAbsolutePath() + " does not denote a data file");
        } else if (!dataFile.canRead()) {
            _log.error("Could not read data file " + dataFile.getAbsolutePath());
        } else {
            try {
                getDataIO().writeDataToDatabase(reader, dataFile.getAbsolutePath());
                _log.info("Written data from file " + dataFile.getAbsolutePath() + " to database");
            } catch (Exception ex) {
                if (isFailOnError()) {
                    throw new BuildException("Could not parse or write data file " + dataFile.getAbsolutePath(), ex);
                } else {
                    _log.error("Could not parse or write data file " + dataFile.getAbsolutePath(), ex);
                }
            }
        }
    }
