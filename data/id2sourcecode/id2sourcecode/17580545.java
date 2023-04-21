    BootImage(boolean ltlEndian, boolean t, String imageCodeFileName, String imageDataFileName, String imageRMapFileName) throws IOException {
        this.imageCodeFileName = imageCodeFileName;
        this.imageDataFileName = imageDataFileName;
        this.imageRMapFileName = imageRMapFileName;
        dataOut = new RandomAccessFile(imageDataFileName, "rw");
        codeOut = new RandomAccessFile(imageCodeFileName, "rw");
        if (mapByteBuffers) {
            bootImageData = dataOut.getChannel().map(MapMode.READ_WRITE, 0, BOOT_IMAGE_DATA_SIZE);
            bootImageCode = codeOut.getChannel().map(MapMode.READ_WRITE, 0, BOOT_IMAGE_CODE_SIZE);
        } else {
            bootImageData = ByteBuffer.allocate(BOOT_IMAGE_DATA_SIZE);
            bootImageCode = ByteBuffer.allocate(BOOT_IMAGE_CODE_SIZE);
        }
        ByteOrder endian = ltlEndian ? ByteOrder.LITTLE_ENDIAN : ByteOrder.BIG_ENDIAN;
        bootImageData.order(endian);
        bootImageCode.order(endian);
        referenceMap = new byte[BOOT_IMAGE_DATA_SIZE >> LOG_BYTES_IN_ADDRESS];
        trace = t;
    }
