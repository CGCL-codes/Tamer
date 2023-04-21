    public static void loadBinaryFile(String Filename, boolean check, ByteBuffer mem, int address) {
        final File binary;
        FileInputStream stream;
        MappedByteBuffer bin;
        binary = new File(Filename);
        try {
            stream = new FileInputStream(binary);
        } catch (FileNotFoundException e) {
            Error.errornum = Error.FILE_NOT_FOUND;
            return;
        }
        try {
            bin = stream.getChannel().map(MapMode.READ_ONLY, 0, binary.length());
            bin.order(ByteOrder.LITTLE_ENDIAN);
        } catch (IOException e) {
            Error.errornum = Error.ioError;
            System.err.println("Error");
            return;
        }
        System.out.println("Writing to Address " + Integer.toHexString(address));
        if (check) {
            if (BinChecker.isUnscrambled(bin)) {
                System.out.println("Binary is Unscrambled");
                System.out.println("Address " + address);
                bin.rewind();
                mem.position(address);
                mem.put(bin);
            } else {
                System.out.println("Binary is Scrambled");
            }
        } else {
            bin.rewind();
            mem.position(address);
            mem.put(bin);
        }
        System.out.println("Binary Loaded  " + Filename);
    }
