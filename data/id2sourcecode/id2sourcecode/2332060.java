    private PF_FileHandle getFileHandle(File f) throws IOException {
        PF_FileHdr hdr = null;
        PF_BufferMgr pBufMgr = null;
        RandomAccessFile ioStream = new RandomAccessFile(f, "rws");
        FileChannel fc = ioStream.getChannel();
        ioStream.seek(0);
        hdr.setNumPages(new PageNum(ioStream.readInt()));
        hdr.setFirstFree(new PageNum(ioStream.readInt()));
        pBufMgr = new PF_BufferMgr();
        return new PF_FileHandle(pBufMgr, hdr, true, true, fc);
    }
