    public void writeDataToDatabase(Platform platform, String[] files) throws DdlUtilsException {
        writeDataToDatabase(platform, platform.readModelFromDatabase("unnamed"), files);
    }
