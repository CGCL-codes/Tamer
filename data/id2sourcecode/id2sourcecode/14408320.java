    public static FirstBytes getFirstBytes(File file) throws IOException {
        if (file.isFile()) {
            boolean truncated = false;
            long size = file.length();
            if (size > 255) {
                truncated = true;
                size = 255;
            }
            FileInputStream stream = new FileInputStream(file);
            FileChannel channel = stream.getChannel();
            ByteBuffer bytes = ByteBuffer.allocate(Ints.checkedCast(size));
            channel.read(bytes);
            channel.close();
            stream.close();
            return new FirstBytes(truncated, bytes.array());
        } else {
            throw new FileNotFoundException("Can't find " + file + '.');
        }
    }
