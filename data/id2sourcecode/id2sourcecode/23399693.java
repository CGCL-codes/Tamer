    protected void assertVS(boolean isWrite) {
        while (!checkVS()) {
            Version v = Version.getVersionLocal();
            new SlotUnknownVersionException(isWrite ? "not loaded for write" : "not loaded for read", null, v).handle();
        }
    }
