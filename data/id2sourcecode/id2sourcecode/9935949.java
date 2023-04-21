    private short getModeValue(String modeString) throws Exception {
        short result = MODE_MAPPING.get(FieldMode.RW.modeString);
        try {
            result = MODE_MAPPING.get(modeString);
        } catch (Exception e) {
            LOG.log("modeString \"" + modeString + "\" is unknown - defaulting to \"readwrite\" (error: " + e.getMessage() + ")");
        }
        return result;
    }
