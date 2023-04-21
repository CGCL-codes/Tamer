    public void verifyMIC(byte[] inTok, int tokOff, int tokLen, byte[] inMsg, int msgOff, int msgLen, MessageProp prop) throws GSSException {
        checkContext();
        logger.debug("enter verifyMic");
        SSLCipherState st = this.conn.getReadCipherState();
        SSLCipherSuite cs = st.getCipherSuite();
        logger.debug("digest algorithm: " + cs.getDigestAlg());
        if (tokLen != (GSI_MESSAGE_DIGEST_PADDING + cs.getDigestOutputLength())) {
            throw new GlobusGSSException(GSSException.DEFECTIVE_TOKEN, GlobusGSSException.TOKEN_FAIL, "tokenFail00", new Object[] { new Integer(tokLen), new Integer(GSI_MESSAGE_DIGEST_PADDING + cs.getDigestOutputLength()) });
        }
        int bufLen = SSLUtil.toInt(inTok, tokOff + GSI_SEQUENCE_SIZE);
        if (bufLen != msgLen) {
            throw new GlobusGSSException(GSSException.DEFECTIVE_TOKEN, GlobusGSSException.TOKEN_FAIL, "tokenFail01", new Object[] { new Integer(msgLen), new Integer(bufLen) });
        }
        int pad_ct = (cs.getDigestOutputLength() == 16) ? 48 : 40;
        byte[] digest = null;
        try {
            MessageDigest md = MessageDigest.getInstance(cs.getDigestAlg());
            md.update(st.getMacKey());
            for (int i = 0; i < pad_ct; i++) {
                md.update(SSLHandshake.pad_1);
            }
            md.update(inTok, tokOff, GSI_MESSAGE_DIGEST_PADDING);
            md.update(inMsg, msgOff, msgLen);
            digest = md.digest();
        } catch (NoSuchAlgorithmException e) {
            throw new GlobusGSSException(GSSException.FAILURE, e);
        }
        byte[] token = new byte[tokLen - GSI_MESSAGE_DIGEST_PADDING];
        System.arraycopy(inTok, tokOff + GSI_MESSAGE_DIGEST_PADDING, token, 0, token.length);
        if (!Arrays.equals(digest, token)) {
            throw new GlobusGSSException(GSSException.BAD_MIC, GlobusGSSException.BAD_MIC, "tokenFail02");
        }
        long tokSeq = SSLUtil.toLong(inTok, tokOff);
        long readSeq = this.conn.getReadSequence();
        long seqTest = tokSeq - readSeq;
        logger.debug("Token seq#   : " + tokSeq);
        logger.debug("Current seq# : " + readSeq);
        if (seqTest > 0) {
            throw new GSSException(GSSException.GAP_TOKEN);
        } else if (seqTest < 0) {
            throw new GSSException(GSSException.OLD_TOKEN);
        } else {
            this.conn.incrementReadSequence();
        }
        if (prop != null) {
            prop.setPrivacy(false);
            prop.setQOP(0);
        }
        logger.debug("exit verifyMic");
    }
