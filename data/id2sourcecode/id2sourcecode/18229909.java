    private int readInt(BufferedInputStream bis) throws IOException, FileFormatErrorException {
        byte[] buf = new byte[4];
        int length = bis.read(buf);
        if (length < 4) throw new FileFormatErrorException(this.indexFileName, fis.getChannel().position());
        int value = 0;
        for (int i = 0; i < 4; i++) {
            value <<= 8;
            value |= (int) buf[i] & 0xFF;
        }
        return value;
    }
