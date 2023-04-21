    public static int toState(String state) {
        if (state.equalsIgnoreCase("read-write")) {
            return Data.READWRITE;
        } else if (state.equalsIgnoreCase("read-only")) {
            return Data.READONLY;
        } else if (state.equalsIgnoreCase("hidden")) {
            return Data.HIDDEN;
        } else {
            Logger.ERROR("Cannot Map Data-state: " + state);
            return Data.READWRITE;
        }
    }
