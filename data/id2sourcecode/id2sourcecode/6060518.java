    public byte[] get_K(byte[] S) {
        byte[] K = new byte[40];
        byte[] hbuf1 = new byte[16];
        byte[] hbuf2 = new byte[16];
        for (int i = 0; i < hbuf1.length; i++) {
            hbuf1[i] = S[i * 2];
            hbuf2[i] = S[(i * 2) + 1];
        }
        byte[] hout1 = getSHA1().digest(hbuf1);
        byte[] hout2 = getSHA1().digest(hbuf2);
        for (int i = 0; i < hout1.length; i++) {
            K[i * 2] = hout1[i];
            K[(i * 2) + 1] = hout2[i];
        }
        return K;
    }
