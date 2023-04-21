    public void writeDataToDatabase(Platform platform, Reader[] inputs) throws DdlUtilsException {
        writeDataToDatabase(platform, platform.readModelFromDatabase("unnamed"), inputs);
    }
