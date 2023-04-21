    public static void outputDebugObject(File dataDir, String postfix, ContentObject object) {
        try {
            if (!dataDir.exists()) {
                if (!dataDir.mkdirs()) {
                    Log.warning("outputDebugData: Cannot create debug data directory: " + dataDir.getAbsolutePath());
                    return;
                }
            }
            byte[] objectDigest = object.digest();
            StringBuffer contentName = new StringBuffer(new BigInteger(1, objectDigest).toString(DEBUG_RADIX));
            if (null != postfix) {
                contentName = contentName.append(postfix);
            }
            contentName.append(".ccnb");
            File outputFile = new File(dataDir, contentName.toString());
            Log.finest("Attempting to output debug data for name " + object.name().toString() + " to file " + outputFile.getAbsolutePath());
            FileOutputStream fos = new FileOutputStream(outputFile);
            try {
                object.encode(fos);
            } finally {
                fos.close();
            }
        } catch (Exception e) {
            Log.warning("Exception attempting to log debug data for name: " + object.name().toString() + " " + e.getClass().getName() + ": " + e.getMessage());
        }
    }
