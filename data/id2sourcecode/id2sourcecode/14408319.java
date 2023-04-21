    public static byte[] getBytes(File file) throws IOException {
        if (file.isFile()) {
            long size = file.length();
            FileInputStream stream = new FileInputStream(file);
            FileChannel channel = stream.getChannel();
            ByteBuffer bytes = ByteBuffer.allocate(Ints.checkedCast(size));
            channel.read(bytes);
            channel.close();
            stream.close();
            return bytes.array();
        } else {
            throw new FileNotFoundException("Can't find " + file + '.');
        }
    }
