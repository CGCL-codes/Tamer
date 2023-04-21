    @Override
    public String getBinaryName() {
        String regPath = GameDatabase.getRegEntry(roomData.getChannel(), roomData.getModName()).get(0);
        return RegistryReader.read(regPath.substring(0, regPath.lastIndexOf("\\") + 1) + "File");
    }
