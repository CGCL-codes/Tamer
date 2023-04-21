    public int encipher(byte[] input, int inofs, int inlen, byte[] output, int outofs) {
        int outlen = inlen + H_length, outbuflen = output.length;
        if (outlen > (outbuflen - outofs)) throw new java.lang.IllegalArgumentException("Output buffer too short, requires (inlen+(2*hashlen [" + H_length + "])+1) bytes (after outofs [" + outofs + "])."); else {
            int oc, ic, rc;
            byte[] r = new byte[H_length];
            rng.nextBytes(r);
            byte[] Gr = G.G(this, r, 0, H_length, inlen);
            for (oc = outofs, ic = inofs, rc = 0; ic < inlen; ic++, rc++, oc++) output[oc] = (byte) ((input[ic] & 0xff) ^ (Gr[rc] & 0xff));
            H.update(output, outofs, inlen);
            byte[] t = H.digest();
            for (oc = inlen, ic = 0, rc = 0; ic < H_length; oc++, ic++, rc++) output[oc] = (byte) ((r[rc] & 0xff) ^ (t[ic] & 0xff));
            return outlen;
        }
    }
