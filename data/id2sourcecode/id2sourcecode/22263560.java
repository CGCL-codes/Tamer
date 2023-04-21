    public void writeDataToXML(Platform platform, DataWriter writer) {
        writeDataToXML(platform, platform.readModelFromDatabase("unnamed"), writer);
    }
