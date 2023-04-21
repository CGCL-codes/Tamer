    public FileOutputChannel(String path) {
        super(path);
        try {
            fileOutputStream = new FileOutputStream(file, false);
            channel = fileOutputStream.getChannel();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }
