    public static byte[] readFileAsByteArray(File file) throws IOException {
        FileChannel fileChannel = new FileInputStream(file).getChannel();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream((int) file.length());
        copyChannel(fileChannel, Channels.newChannel(outputStream));
        fileChannel.close();
        outputStream.close();
        byte[] data = outputStream.toByteArray();
        return data;
    }
