    public static FileChannel openFileChannelForReading(String pFilePath) throws Exception {
        File f = new File(pFilePath);
        FileInputStream fis = new FileInputStream(f);
        return fis.getChannel();
    }
