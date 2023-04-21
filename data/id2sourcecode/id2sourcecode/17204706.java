    public int Process(SocketChannel chan, ByteBuffer Buffer) throws IOException {
        int len = -1;
        long remain = FileLen - CurFile.length();
        if (remain > 0) {
            FileOutputStream fos = new FileOutputStream(CurFile, true);
            FileChannel foc = fos.getChannel();
            do {
                Buffer.clear();
                if (remain < Buffer.remaining()) {
                    Buffer.limit((int) remain);
                }
                len = chan.read(Buffer);
                Buffer.flip();
                foc.write(Buffer);
                if (len > 0) {
                    remain -= len;
                }
            } while (len > 0 && remain > 0);
            foc.close();
        }
        if (remain <= 0) {
            Complete = true;
            Started = false;
            RC.Complete(this, CurFile);
            Next();
        }
        return len;
    }
