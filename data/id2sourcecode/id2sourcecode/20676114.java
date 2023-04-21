    protected void readwriteCheck(Regatta reg) throws java.io.IOException {
        new RegattaManager(reg).writeRegattaToDisk(Util.getWorkingDirectory(), "test.regatta");
        Regatta reg2 = RegattaManager.readRegattaFromDisk(Util.getWorkingDirectory(), "test.regatta");
        assertEquals("readwrite " + reg.getName(), reg, reg2);
    }
