    private ByteBuffer getByteBuffer(String filename) {
        ByteBuffer buffer = null;
        try {
            long filesize = (new File(filename)).length();
            System.out.println("FileSize=" + filesize);
            final FileInputStream fis = new FileInputStream(filename);
            FileChannel fc = fis.getChannel();
            buffer = ByteBuffer.allocate((int) filesize);
            buffer.order(ByteOrder.LITTLE_ENDIAN);
            int bytesRead = fc.read(buffer);
            System.out.println("bytesRead=" + bytesRead);
        } catch (Exception e) {
            String msg = e.getMessage();
            if (null != msg) System.out.println(msg);
            System.out.println(e.getStackTrace());
        }
        return buffer;
    }
