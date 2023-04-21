    public void writeDataToDatabase(Platform platform, InputStream[] inputs) throws DdlUtilsException {
        writeDataToDatabase(platform, platform.readModelFromDatabase("unnamed"), inputs);
    }
