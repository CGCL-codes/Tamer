    private ByteBuffer getResourceAsBuffer(File f) {
        ByteBuffer buf = null;
        try {
            FileInputStream fis = new FileInputStream(f);
            FileChannel channel = fis.getChannel();
            buf = ByteBuffer.allocate((int) channel.size());
            channel.read(buf);
            buf.flip();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return buf;
    }
