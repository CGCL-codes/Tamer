    public byte[] getPayLoad(SshCrypto xcrypt, long seqnr) throws IOException {
        byte[] data = getData();
        int blocksize = 8;
        packet_length = 4 + 1 + 1;
        if (data != null) packet_length += data.length;
        int padlen = blocksize - (packet_length % blocksize);
        if (padlen < 4) padlen += blocksize;
        byte[] padding = new byte[padlen];
        System.out.println("packet length is " + packet_length + ", padlen is " + padlen);
        if (xcrypt == null) for (int i = 0; i < padlen; i++) padding[i] = 0; else for (int i = 0; i < padlen; i++) padding[i] = SshMisc.getNotZeroRandomByte();
        byte[] block = new byte[packet_length + padlen];
        int xlen = padlen + packet_length - 4;
        block[3] = (byte) (xlen & 0xff);
        block[2] = (byte) ((xlen >> 8) & 0xff);
        block[1] = (byte) ((xlen >> 16) & 0xff);
        block[0] = (byte) ((xlen >> 24) & 0xff);
        block[4] = (byte) padlen;
        block[5] = getType();
        System.arraycopy(data, 0, block, 6, data.length);
        System.arraycopy(padding, 0, block, 6 + data.length, padlen);
        byte[] md5sum;
        if (xcrypt != null) {
            MessageDigest md5 = null;
            try {
                md5 = MessageDigest.getInstance("MD5");
            } catch (NoSuchAlgorithmException e) {
                System.err.println("SshPacket2: unable to load message digest algorithm: " + e);
            }
            byte[] seqint = new byte[4];
            seqint[0] = (byte) ((seqnr >> 24) & 0xff);
            seqint[1] = (byte) ((seqnr >> 16) & 0xff);
            seqint[2] = (byte) ((seqnr >> 8) & 0xff);
            seqint[3] = (byte) ((seqnr) & 0xff);
            md5.update(seqint, 0, 4);
            md5.update(block, 0, block.length);
            md5sum = md5.digest();
        } else {
            md5sum = new byte[0];
        }
        if (xcrypt != null) block = xcrypt.encrypt(block);
        byte[] sendblock = new byte[block.length + md5sum.length];
        System.arraycopy(block, 0, sendblock, 0, block.length);
        System.arraycopy(md5sum, 0, sendblock, block.length, md5sum.length);
        return sendblock;
    }
