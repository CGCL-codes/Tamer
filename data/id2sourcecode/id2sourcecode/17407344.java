    public int decipher(byte[] input, int inofs, int inlen, byte[] output, int outofs) {
        int slen = inlen - H_length;
        int it = inofs + slen;
        byte[] Hs;
        H.update(input, inofs, it);
        Hs = H.digest();
        byte[] r = new byte[H_length];
        for (int ic = it, ir = 0; ir < H_length; ic++, ir++) r[ir] = (byte) ((Hs[ir] & 0xff) ^ (input[ic] & 0xff));
        byte[] Gr = G.G(this, r, 0, H_length, slen);
        for (int oc = outofs, ic = inofs, ir = 0; ir < slen; oc++, ic++, ir++) output[oc] = (byte) ((input[ic] & 0xff) ^ (Gr[ir] & 0xff));
        return slen;
    }
