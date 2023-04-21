    public void printInformation(String category, PrintStream ps) throws Exception {
        if (category.equalsIgnoreCase("DATASTORE")) {
            ps.println(LOCALISER.msg("032020", storeManagerKey, getConnectionURL(), (readOnlyDatastore ? "read-only" : "read-write"), (fixedDatastore ? ", fixed" : "")));
        }
    }
