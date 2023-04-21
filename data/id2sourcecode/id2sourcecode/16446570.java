    public static void writeFile(ByteBuffer data, File destination) throws IOException {
        FileChannel fileChannel = new FileOutputStream(destination).getChannel();
        try {
            fileChannel.write(data);
        } finally {
            fileChannel.close();
        }
    }
