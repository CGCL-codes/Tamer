    public byte[] getMIC(byte[] inBuf, int off, int len, MessageProp prop) throws GSSException {
        checkContext();
        logger.debug("enter getMic");
        if (prop != null && (prop.getQOP() != 0 || prop.getPrivacy())) {
            throw new GSSException(GSSException.BAD_QOP);
        }
        SSLCipherState st = this.conn.getWriteCipherState();
        SSLCipherSuite cs = st.getCipherSuite();
        long sequence = this.conn.getWriteSequence();
        byte[] mic = new byte[GSI_MESSAGE_DIGEST_PADDING + cs.getDigestOutputLength()];
        System.arraycopy(Util.toBytes(sequence), 0, mic, 0, GSI_SEQUENCE_SIZE);
        System.arraycopy(Util.toBytes(len, 4), 0, mic, GSI_SEQUENCE_SIZE, 4);
        this.conn.incrementWriteSequence();
        int pad_ct = (cs.getDigestOutputLength() == 16) ? 48 : 40;
        try {
            MessageDigest md = MessageDigest.getInstance(cs.getDigestAlg());
            md.update(st.getMacKey());
            for (int i = 0; i < pad_ct; i++) {
                md.update(SSLHandshake.pad_1);
            }
            md.update(mic, 0, GSI_MESSAGE_DIGEST_PADDING);
            md.update(inBuf, off, len);
            byte[] digest = md.digest();
            System.arraycopy(digest, 0, mic, GSI_MESSAGE_DIGEST_PADDING, digest.length);
        } catch (NoSuchAlgorithmException e) {
            throw new GlobusGSSException(GSSException.FAILURE, e);
        }
        if (prop != null) {
            prop.setPrivacy(false);
            prop.setQOP(0);
        }
        logger.debug("exit getMic");
        return mic;
    }
