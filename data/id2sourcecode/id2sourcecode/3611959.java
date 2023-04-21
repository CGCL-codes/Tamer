    private byte[] computeFinished(byte who) throws IOException {
        byte[] sender[] = { { 0x53, 0x52, 0x56, 0x52 }, { 0x43, 0x4c, 0x4e, 0x54 } };
        byte[] msg = new byte[MD5_SIZE + SHA_SIZE];
        byte[] tmp = null;
        try {
            MessageDigest d = (MessageDigest) ourMD5.clone();
            d.update(sender[who], 0, 4);
            d.update(master, 0, master.length);
            tmp = new byte[MD5_SIZE];
            d.update(MAC.PAD1, 0, 48);
            d.digest(tmp, 0, tmp.length);
            d.update(master, 0, master.length);
            d.update(MAC.PAD2, 0, 48);
            d.update(tmp, 0, tmp.length);
            d.digest(msg, 0, MD5_SIZE);
            d = (MessageDigest) ourSHA.clone();
            d.update(sender[who], 0, 4);
            d.update(master, 0, master.length);
            tmp = new byte[SHA_SIZE];
            d.update(MAC.PAD1, 0, 40);
            d.digest(tmp, 0, tmp.length);
            d.update(master, 0, master.length);
            d.update(MAC.PAD2, 0, 40);
            d.update(tmp, 0, tmp.length);
            d.digest(msg, MD5_SIZE, SHA_SIZE);
            return msg;
        } catch (Exception e) {
            throw new IOException("MessageDigest not cloneable");
        }
    }
