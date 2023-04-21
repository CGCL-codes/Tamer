    public Channel channel() {
        try {
            FileInputStream fis = new FileInputStream(file);
            FileChannel ch = fis.getChannel();
            return ch;
        } catch (FileNotFoundException ex) {
            ex.printStackTrace();
        }
        return null;
    }
