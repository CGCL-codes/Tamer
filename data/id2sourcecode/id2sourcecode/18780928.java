    public CMPPConnectMessage(byte bindType) {
        this.authenticatorSA = CMPPConfig.getAuthenticatorSA();
        this.sharedSecret = CMPPConfig.getSharedSecret();
        this.bindType = bindType;
        commandId = ProtocolCommandID.CMPP_CONNECT;
        String strNow = null;
        if (CMPPConfig.isSMIAS()) {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
            Date now = new Date();
            strNow = dateFormat.format(now);
            timestamp = (int) (new Date().getTime() / 1000);
        } else {
            SimpleDateFormat dateFormat = new SimpleDateFormat("MMddHHmmss");
            Date now = new Date();
            strNow = dateFormat.format(now);
            timestamp = new Integer(strNow).intValue();
        }
        byte[] b0 = new byte[9];
        for (int i = 0; i < 9; i++) {
            b0[i] = 0;
        }
        try {
            ByteBuffer bb = ByteBuffer.allocate(32);
            bb.put(authenticatorSA.getBytes());
            bb.put(b0);
            bb.put(sharedSecret.getBytes());
            if (CMPPConfig.isSMIAS()) {
                bb.putInt(timestamp);
            } else {
                bb.put(strNow.getBytes());
            }
            bb.flip();
            byte[] toMd5 = new byte[bb.limit()];
            bb.get(toMd5);
            MessageDigest md = MessageDigest.getInstance("MD5");
            authenticatorSource = md.digest(toMd5);
            logger.info("authenticatorSource: " + ByteUtil.toHexForLog(authenticatorSource));
        } catch (NoSuchAlgorithmException e) {
            logger.error("I don't know how to compute MD5!");
            System.exit(1);
        }
    }
