    private static final void readFile(String name, ByteBuffer mem, int address) {
        FileInputStream stream;
        try {
            stream = new FileInputStream(name);
        } catch (FileNotFoundException e) {
            Error.errornum = Error.FILE_NOT_FOUND;
            return;
        }
        try {
            stream.getChannel().read(mem, address);
            stream.close();
        } catch (IOException e) {
            Error.errornum = Error.ioError;
            return;
        }
    }
