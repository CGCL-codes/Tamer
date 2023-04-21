    private void moveToExpectedPath(File dbFile) throws IOException {
        File parentDir = dbPath.getParentFile();
        String dbName = dbPath.getName();
        File destFile = new File(parentDir, dbFile.getName().replaceAll("h2db\\d*", dbName));
        getLog().debug("Creating file from template: " + destFile);
        FileUtils.copyFile(dbFile, destFile);
        dbFile.delete();
    }
