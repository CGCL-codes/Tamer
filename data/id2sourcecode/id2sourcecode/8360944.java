    public void read(InputStream in) throws IOException {
        short strLen = WMFConstants.readLittleEndianShort(in);
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        for (int i = 0; i < strLen; i++) baos.write(in.read());
        if ((strLen % 2) == 1) in.read();
        this.text = new String(baos.toByteArray());
        this.y = WMFConstants.readLittleEndianShort(in);
        this.x = WMFConstants.readLittleEndianShort(in);
    }
