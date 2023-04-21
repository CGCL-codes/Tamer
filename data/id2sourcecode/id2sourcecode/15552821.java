    public FileInputChannel(String path) {
        super(path);
        try {
            fileInputStream = new FileInputStream(file);
            channel = fileInputStream.getChannel();
            buffer.flip();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
