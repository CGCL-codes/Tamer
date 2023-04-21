    public ReturnCode writeFileHdr() {
        FileOutputStream out = new FileOutputStream(this.descriptor);
        FileChannel channel = out.getChannel();
        ByteBuffer bbuf = ByteBuffer.allocate(8);
        bbuf.putInt(this.hdr.getFirstFree().getPageNum());
        bbuf.putInt(this.hdr.getNumPages().getPageNum());
        try {
            channel.write(bbuf, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return ReturnCode.PF_SUCCESS;
    }
