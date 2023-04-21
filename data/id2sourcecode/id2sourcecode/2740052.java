    public EasyReadingFile(String path) {
        FileInputStream stream;
        try {
            File file = new File(Constantes.DATA_PATH + path);
            stream = new FileInputStream(file);
        } catch (FileNotFoundException e) {
            throw new RuntimeException("Unable to find " + path);
        }
        FileChannel chIn = stream.getChannel();
        try {
            data = ByteBuffer.allocate((int) chIn.size());
            chIn.read(data);
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException("Unable to read " + path);
        }
        data.flip();
    }
