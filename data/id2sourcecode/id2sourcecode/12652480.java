    public byte[] encodeBinary(Reader reader) {
        if (reader == null) {
            return null;
        }
        byte[] fileDigest = null;
        try {
            ReaderInputStream ris = new ReaderInputStream(reader);
            byte[] buf = new byte[BUFFER_SIZE];
            this.md.reset();
            DigestInputStream dis = new DigestInputStream(ris, this.md);
            dis.on(true);
            while (dis.read(buf, 0, BUFFER_SIZE) != -1) ;
            dis.close();
            ris.close();
            ris = null;
            fileDigest = this.md.digest();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
        return fileDigest;
    }
