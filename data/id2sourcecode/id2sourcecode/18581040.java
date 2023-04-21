    private ByteBuffer readToBuffer() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        byte[] buf = new byte[512];
        int read = 0;
        try {
            while ((read = vorbisStream.readPcm(buf, 0, buf.length)) > 0) {
                baos.write(buf, 0, read);
            }
        } catch (EndOfOggStreamException ex) {
        }
        byte[] dataBytes = baos.toByteArray();
        swapBytes(dataBytes, 0, dataBytes.length);
        int bytesToCopy = getOggTotalBytes(dataBytes.length);
        ByteBuffer data = BufferUtils.createByteBuffer(bytesToCopy);
        data.put(dataBytes, 0, bytesToCopy).flip();
        vorbisStream.close();
        loStream.close();
        oggStream.close();
        return data;
    }
