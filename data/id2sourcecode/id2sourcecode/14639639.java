    protected ByteBuffer getData(String filename) {
        ByteBuffer buffer = null;
        System.out.println("Attempting to load: " + filename);
        try {
            BufferedInputStream bis = new BufferedInputStream(WaveData.class.getClassLoader().getResourceAsStream(filename));
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            int bufferLength = 4096;
            byte[] readBuffer = new byte[bufferLength];
            int read = -1;
            while ((read = bis.read(readBuffer, 0, bufferLength)) != -1) {
                baos.write(readBuffer, 0, read);
            }
            bis.close();
            buffer = ByteBuffer.allocateDirect(baos.size());
            buffer.order(ByteOrder.nativeOrder());
            buffer.put(baos.toByteArray());
            buffer.rewind();
        } catch (Exception ioe) {
            ioe.printStackTrace();
        }
        return buffer;
    }
