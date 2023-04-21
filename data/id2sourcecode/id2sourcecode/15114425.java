    public cdi(String file) throws FileNotFoundException {
        read = new RandomAccessFile(file, "r");
        try {
            length = read.length();
            fileChannel = read.getChannel();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
