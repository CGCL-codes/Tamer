    private ByteBuffer load(java.net.URL url) {
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(url.openStream());
            DataInputStream dataStream = new DataInputStream(bufferedInputStream);
            byte shaderCode[] = new byte[bufferedInputStream.available()];
            dataStream.readFully(shaderCode);
            bufferedInputStream.close();
            dataStream.close();
            ByteBuffer shaderByteBuffer = BufferUtils.createByteBuffer(shaderCode.length);
            shaderByteBuffer.put(shaderCode);
            shaderByteBuffer.rewind();
            return shaderByteBuffer;
        } catch (Exception e) {
            logger.severe("Could not load shader object: " + e);
            logger.logp(Level.SEVERE, getClass().getName(), "load(URL)", "Exception", e);
            return null;
        }
    }
