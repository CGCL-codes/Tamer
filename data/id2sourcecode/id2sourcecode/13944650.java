    public void indexSetComplete() throws IOException {
        outputChannel.close();
        fos.close();
        fis = new FileInputStream(file);
        inputChannel = fis.getChannel();
    }
