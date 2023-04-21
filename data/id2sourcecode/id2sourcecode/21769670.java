    public static WriteStatusThread create(CowboyArch owner, boolean writeLocations, boolean dumpAgsMind) {
        if (instance == null) {
            instance = new WriteStatusThread();
            instance.owner = owner;
            instance.writeLocations = writeLocations;
            instance.dumpAgsMinds = dumpAgsMind;
            instance.start();
        }
        return instance;
    }
