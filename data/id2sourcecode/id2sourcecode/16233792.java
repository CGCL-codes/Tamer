    public static FileChannel openFileChannelForAppend(String pFilePath) throws Exception {
        File f = new File(pFilePath);
        FileOutputStream fos = new FileOutputStream(f, true);
        return fos.getChannel();
    }
