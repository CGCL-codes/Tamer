    public int Process(SocketChannel chan, ByteBuffer Buffer) throws IOException {
        System.out.println(" Processing " + this.toString() + " Processing!");
        int len = -1;
        FileInputStream fis = new FileInputStream(Data);
        FileChannel fic = fis.getChannel();
        fic.position(CurPos);
        Buffer.clear();
        int filelen = fic.read(Buffer);
        Buffer.flip();
        do {
            if (!Buffer.hasRemaining()) {
                Buffer.clear();
                filelen = fic.read(Buffer);
                Buffer.flip();
            }
            len = chan.write(Buffer);
        } while (len > 0 && filelen > 0);
        CurPos = fic.position() - Buffer.remaining();
        fic.close();
        if (CurPos == Data.length()) {
            System.out.println("FILE SENT " + this.toString());
            if (this.isDeleteOnComplete()) {
                Data.delete();
            }
            Complete = true;
            Started = false;
            Next();
        }
        return len;
    }
