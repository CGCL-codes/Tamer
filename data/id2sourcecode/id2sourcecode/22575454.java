    public RC writeFileHdr() {
        FileOutputStream out = new FileOutputStream(this.descriptor);
        FileChannel channel = out.getChannel();
        ByteBuffer bbuf = ByteBuffer.allocate(8);
        bbuf.putInt(this.hdr.getFirstFree());
        bbuf.putInt(this.hdr.getNumPages());
        try {
            channel.write(bbuf, 0);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return RC.PF_SUCCESS;
    }
