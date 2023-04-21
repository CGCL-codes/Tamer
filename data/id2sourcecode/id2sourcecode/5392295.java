    public static boolean verifySignature(byte[] message, RSASignature ds, RSAPublicKey dpk) throws IOException {
        SHA1 md = new SHA1();
        md.update(message);
        byte[] sha_message = new byte[md.getDigestLength()];
        md.digest(sha_message);
        BigInteger n = dpk.getN();
        BigInteger e = dpk.getE();
        BigInteger s = ds.getS();
        if (n.compareTo(s) <= 0) {
            log.log(20, "ssh-rsa signature: n.compareTo(s) <= 0");
            return false;
        }
        int rsa_block_len = (n.bitLength() + 7) / 8;
        if (rsa_block_len < 1) {
            log.log(20, "ssh-rsa signature: rsa_block_len < 1");
            return false;
        }
        byte[] v = s.modPow(e, n).toByteArray();
        int startpos = 0;
        if ((v.length > 0) && (v[0] == 0x00)) startpos++;
        if ((v.length - startpos) != (rsa_block_len - 1)) {
            log.log(20, "ssh-rsa signature: (v.length - startpos) != (rsa_block_len - 1)");
            return false;
        }
        if (v[startpos] != 0x01) {
            log.log(20, "ssh-rsa signature: v[startpos] != 0x01");
            return false;
        }
        int pos = startpos + 1;
        while (true) {
            if (pos >= v.length) {
                log.log(20, "ssh-rsa signature: pos >= v.length");
                return false;
            }
            if (v[pos] == 0x00) break;
            if (v[pos] != (byte) 0xff) {
                log.log(20, "ssh-rsa signature: v[pos] != (byte) 0xff");
                return false;
            }
            pos++;
        }
        int num_pad = pos - (startpos + 1);
        if (num_pad < 8) {
            log.log(20, "ssh-rsa signature: num_pad < 8");
            return false;
        }
        pos++;
        if (pos >= v.length) {
            log.log(20, "ssh-rsa signature: pos >= v.length");
            return false;
        }
        SimpleDERReader dr = new SimpleDERReader(v, pos, v.length - pos);
        byte[] seq = dr.readSequenceAsByteArray();
        if (dr.available() != 0) {
            log.log(20, "ssh-rsa signature: dr.available() != 0");
            return false;
        }
        dr.resetInput(seq);
        byte digestAlgorithm[] = dr.readSequenceAsByteArray();
        if ((digestAlgorithm.length < 8) || (digestAlgorithm.length > 9)) {
            log.log(20, "ssh-rsa signature: (digestAlgorithm.length < 8) || (digestAlgorithm.length > 9)");
            return false;
        }
        byte[] digestAlgorithm_sha1 = new byte[] { 0x06, 0x05, 0x2b, 0x0e, 0x03, 0x02, 0x1a, 0x05, 0x00 };
        for (int i = 0; i < digestAlgorithm.length; i++) {
            if (digestAlgorithm[i] != digestAlgorithm_sha1[i]) {
                log.log(20, "ssh-rsa signature: digestAlgorithm[i] != digestAlgorithm_sha1[i]");
                return false;
            }
        }
        byte[] digest = dr.readOctetString();
        if (dr.available() != 0) {
            log.log(20, "ssh-rsa signature: dr.available() != 0 (II)");
            return false;
        }
        if (digest.length != sha_message.length) {
            log.log(20, "ssh-rsa signature: digest.length != sha_message.length");
            return false;
        }
        for (int i = 0; i < sha_message.length; i++) {
            if (sha_message[i] != digest[i]) {
                log.log(20, "ssh-rsa signature: sha_message[i] != digest[i]");
                return false;
            }
        }
        return true;
    }
