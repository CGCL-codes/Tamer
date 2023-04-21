    private static void initTDReadOnly() {
        Boolean oldReadOnlyTD = readOnlyTD;
        readOnlyTD = !Config.TD_READ_ONLY.equals("false");
        if (oldReadOnlyTD != readOnlyTD) {
            logger.info("TD is now " + (readOnlyTD ? "read-only" : "read-write"));
        }
    }
