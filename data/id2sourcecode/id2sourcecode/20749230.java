    public static void writeFile(File file, byte[] data) throws FileNotFoundException, IOException {
        requireFile(file);
        createFile(file);
        FileChannel dest = new FileOutputStream(file).getChannel();
        dest.write(ByteBuffer.wrap(data));
        dest.close();
    }
