    public static void writeByteArrayToFile(File file, byte[] buffer) throws IOException {
        FileChannel fileChannel = new FileOutputStream(file).getChannel();
        ByteArrayInputStream inputStream = new ByteArrayInputStream(buffer);
        copyChannel(Channels.newChannel(inputStream), fileChannel);
        fileChannel.close();
        inputStream.close();
    }
