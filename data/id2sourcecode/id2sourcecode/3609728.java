    public static void outputDebugData(ContentName name, byte[] data) {
        try {
            File dataDir = new File(DEBUG_DATA_DIRECTORY);
            if (!dataDir.exists()) {
                if (!dataDir.mkdirs()) {
                    Log.warning("outputDebugData: Cannot create default debug data directory: " + dataDir.getAbsolutePath());
                    return;
                }
            }
            File outputParent = new File(dataDir, name.toString());
            if (!outputParent.exists()) {
                if (!outputParent.mkdirs()) {
                    Log.warning("outputDebugData: cannot create data parent directory: " + outputParent);
                }
            }
            byte[] contentDigest = CCNDigestHelper.digest(data);
            String contentName = new BigInteger(1, contentDigest).toString(DEBUG_RADIX);
            File outputFile = new File(outputParent, contentName);
            Log.finest("Attempting to output debug data for name " + name.toString() + " to file " + outputFile.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(outputFile);
            try {
                fos.write(data);
            } finally {
                fos.close();
            }
        } catch (Exception e) {
            Log.warning("Exception attempting to log debug data for name: " + name.toString() + " " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
