    protected void doRealize() throws MediaException {
        curTime = 0;
        if (stream == null) return;
        int chunksize = 128;
        byte[] tmpseqs = new byte[chunksize];
        byte[] seqs = null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream(chunksize);
        try {
            int read;
            while ((read = stream.read(tmpseqs, 0, chunksize)) != -1) {
                baos.write(tmpseqs, 0, read);
            }
            seqs = baos.toByteArray();
            baos.close();
            tmpseqs = null;
            System.gc();
        } catch (IOException ex) {
            throw new MediaException("fail to read from source");
        }
        this.setSequence(seqs);
    }
