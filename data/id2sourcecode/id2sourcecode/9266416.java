    private byte[] wrapCommandAPDU(byte[] capdu, int len) throws GeneralSecurityException, IOException {
        if (capdu == null || capdu.length < 4 || len < 4) {
            throw new IllegalArgumentException("Invalid type");
        }
        int lc = 0;
        int le = capdu[len - 1] & 0x000000FF;
        if (len == 4) {
            lc = 0;
            le = 0;
        } else if (len == 5) {
            lc = 0;
        } else if (len > 5) {
            lc = capdu[ISO7816.OFFSET_LC] & 0x000000FF;
        }
        if (4 + lc >= len) {
            le = 0;
        }
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        byte[] maskedHeader = new byte[4];
        System.arraycopy(capdu, 0, maskedHeader, 0, 4);
        maskedHeader[ISO7816.OFFSET_CLA] = (byte) (capdu[ISO7816.OFFSET_CLA] | 0x0C);
        byte[] paddedHeader = Util.pad(maskedHeader);
        byte[] do87 = new byte[0];
        byte[] do8E = new byte[0];
        byte[] do97 = new byte[0];
        if (le > 0) {
            out.reset();
            out.write((byte) 0x97);
            out.write((byte) 0x01);
            out.write((byte) le);
            do97 = out.toByteArray();
        }
        if (lc > 0) {
            byte[] data = Util.pad(capdu, ISO7816.OFFSET_CDATA, lc);
            cipher.init(Cipher.ENCRYPT_MODE, ksEnc, ZERO_IV_PARAM_SPEC);
            byte[] ciphertext = cipher.doFinal(data);
            out.reset();
            out.write((byte) 0x87);
            out.write(BERTLVObject.getLengthAsBytes(ciphertext.length + 1));
            out.write(0x01);
            out.write(ciphertext, 0, ciphertext.length);
            do87 = out.toByteArray();
        }
        out.reset();
        out.write(paddedHeader, 0, paddedHeader.length);
        out.write(do87, 0, do87.length);
        out.write(do97, 0, do97.length);
        byte[] m = out.toByteArray();
        out.reset();
        DataOutputStream dataOut = new DataOutputStream(out);
        ssc++;
        dataOut.writeLong(ssc);
        dataOut.write(m, 0, m.length);
        dataOut.flush();
        byte[] n = Util.pad(out.toByteArray());
        mac.init(ksMac);
        byte[] cc = mac.doFinal(n);
        out.reset();
        out.write((byte) 0x8E);
        out.write(cc.length);
        out.write(cc, 0, cc.length);
        do8E = out.toByteArray();
        out.reset();
        out.write(maskedHeader, 0, 4);
        out.write((byte) (do87.length + do97.length + do8E.length));
        out.write(do87, 0, do87.length);
        out.write(do97, 0, do97.length);
        out.write(do8E, 0, do8E.length);
        out.write(0x00);
        return out.toByteArray();
    }
