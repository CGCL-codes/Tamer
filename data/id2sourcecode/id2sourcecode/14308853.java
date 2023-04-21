    private boolean isId3v22Tag() throws IOException {
        int id3tagsize;
        ID3v22Tag id3tag = new ID3v22Tag();
        ByteBuffer bb = ByteBuffer.allocate(AbstractID3v2Tag.TAG_HEADER_LENGTH);
        raf.seek(0);
        raf.getChannel().read(bb);
        if (id3tag.seek(bb)) {
            id3tagsize = id3tag.readSize(bb);
            raf.seek(id3tagsize);
            if (isFlacHeader()) {
                return true;
            }
        }
        return false;
    }
