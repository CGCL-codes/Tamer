    private static byte[] getByteArrayFromFile(File file) {
        int bufferSize = 1024 * 8;
        ByteBuffer inBuf = ByteBuffer.allocate(bufferSize);
        ByteArrayOutputStream outBuf = new ByteArrayOutputStream(bufferSize);
        int numRead = 0;
        try {
            FileChannel fc = new FileInputStream(file).getChannel();
            while ((numRead = fc.read(inBuf)) >= 0) {
                inBuf.flip();
                outBuf.write(inBuf.array());
                inBuf.clear();
            }
            outBuf.flush();
            outBuf.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return outBuf.toByteArray();
    }
