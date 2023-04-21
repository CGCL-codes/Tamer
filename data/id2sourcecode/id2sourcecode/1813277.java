    public boolean fromResource(String resourceLocation, String outputFile, Hashtable variables) throws IOException {
        String batchFile = null;
        byte[] batchFileChars = null;
        try {
            batchFileChars = ResourceLoader.instance().getBytesFromResourceLocation(resourceLocation);
        } catch (Exception e) {
            logger.error("Could not find batch file " + resourceLocation + " in resource path...");
            return false;
        }
        if (batchFileChars == null) {
            return false;
        } else {
            batchFile = String.valueOf(batchFileChars);
            StringReader sreader = new StringReader(batchFile);
            logger.debug("Try to create batch file: " + outputFile);
            FileWriter writer = new FileWriter(outputFile);
            createBatchFile(sreader, writer, variables);
        }
        return true;
    }
