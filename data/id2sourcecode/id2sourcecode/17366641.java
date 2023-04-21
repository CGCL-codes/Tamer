    public void init() throws Exception {
        if (!destFile.exists()) {
            destFile.createNewFile();
        }
        destination = new FileOutputStream(destFile).getChannel();
        bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(destFile)));
    }
