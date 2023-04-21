    public static void checkOutput(String filename, boolean overwrite) throws Exception {
        File file = new File(filename);
        if (!overwrite && file.exists()) throw new Exception("The file " + filename + " already exists!");
        if (file.exists() && (file.isDirectory() || !file.canWrite())) throw new Exception("The system can not write in " + filename + "!");
    }
