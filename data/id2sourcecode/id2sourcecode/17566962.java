    public static void checkOutput(String filename, boolean overwrite) throws EDITSException {
        File file = new File(filename);
        if (!overwrite && file.exists()) throw new EDITSException("The file " + filename + " already exists!");
        if (file.exists() && (file.isDirectory() || !file.canWrite())) throw new EDITSException("The system can not write in " + filename + "!");
    }
