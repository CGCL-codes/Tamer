    public Channel channel() {
        try {
            FileInputStream fis = new FileInputStream(realFile);
            FileChannel ch = fis.getChannel();
            return ch;
        } catch (FileNotFoundException e) {
            return null;
        }
    }
