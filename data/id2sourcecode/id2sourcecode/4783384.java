    public void open(File f) throws IOException {
        file = f;
        fos = new FileOutputStream(f);
        outputChannel = fos.getChannel();
        fis = new FileInputStream(file);
        inputChannel = fis.getChannel();
    }
