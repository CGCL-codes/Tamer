    private static void writeTargaFile(String filename, ByteBuffer data, int width, int height) {
        try {
            FileOutputStream fos = new FileOutputStream(new File(filename));
            ByteBuffer header = ByteBuffer.allocate(TARGA_HEADER_SIZE);
            header.put(0, (byte) 0).put(1, (byte) 0);
            header.put(2, (byte) 2);
            header.put(12, (byte) (width & 0xFF));
            header.put(13, (byte) (width >> 8));
            header.put(14, (byte) (height & 0xFF));
            header.put(15, (byte) (height >> 8));
            header.put(16, (byte) 24);
            fos.getChannel().write(header);
            fos.getChannel().write(data);
            data.clear();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
