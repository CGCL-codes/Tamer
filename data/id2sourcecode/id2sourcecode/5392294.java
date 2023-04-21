    public static RSASignature generateSignature(byte[] message, RSAPrivateKey pk) throws IOException {
        SHA1 md = new SHA1();
        md.update(message);
        byte[] sha_message = new byte[md.getDigestLength()];
        md.digest(sha_message);
        byte[] der_header = new byte[] { 0x30, 0x21, 0x30, 0x09, 0x06, 0x05, 0x2b, 0x0e, 0x03, 0x02, 0x1a, 0x05, 0x00, 0x04, 0x14 };
        int rsa_block_len = (pk.getN().bitLength() + 7) / 8;
        int num_pad = rsa_block_len - (2 + der_header.length + sha_message.length) - 1;
        if (num_pad < 8) throw new IOException("Cannot sign with RSA, message too long");
        byte[] sig = new byte[der_header.length + sha_message.length + 2 + num_pad];
        sig[0] = 0x01;
        for (int i = 0; i < num_pad; i++) {
            sig[i + 1] = (byte) 0xff;
        }
        sig[num_pad + 1] = 0x00;
        System.arraycopy(der_header, 0, sig, 2 + num_pad, der_header.length);
        System.arraycopy(sha_message, 0, sig, 2 + num_pad + der_header.length, sha_message.length);
        BigInteger m = new BigInteger(1, sig);
        BigInteger s = m.modPow(pk.getD(), pk.getN());
        return new RSASignature(s);
    }
