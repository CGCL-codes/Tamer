    FileObjectDiskChannel(String fileName, String mode) throws FileNotFoundException {
        this.name = fileName;
        RandomAccessFile file = new RandomAccessFile(fileName, mode);
        channel = file.getChannel();
    }
