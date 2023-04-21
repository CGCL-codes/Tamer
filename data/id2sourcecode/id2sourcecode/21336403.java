    public static void main(String[] args) {
        try {
            byte[] testGB = new byte[] { (byte) 0xB2, (byte) 0xE2, (byte) 0xD1, (byte) 0xE9 };
            System.out.println(new String(testGB, "gb2312"));
        } catch (Exception e) {
            e.printStackTrace();
        }
        Logger logger = Logger.getLogger(ServerSessionHandler.class);
        byte[] authenticatorSource = null;
        byte[] b0 = new byte[9];
        for (int i = 0; i < 9; i++) {
            b0[i] = 0;
        }
        String sourceAddr = "923122";
        String sharedSecret = "03817";
        int LEN_SS = 5;
        String strNow = "0612143008";
        try {
            MessageDigest md = MessageDigest.getInstance("MD5");
            ByteBuffer bb = ByteBuffer.allocate(32);
            bb.put(sourceAddr.getBytes());
            bb.put(b0);
            bb.put(sharedSecret.getBytes());
            bb.put(strNow.getBytes());
            bb.flip();
            byte[] arr = new byte[bb.limit()];
            bb.get(arr);
            authenticatorSource = md.digest(arr);
            logger.debug("authenticatorSource: " + ByteUtil.toHexForLog(authenticatorSource));
            ByteBuffer bb_resp = ByteBuffer.allocate(32);
            byte status = 0x00;
            bb_resp.put(status);
            bb_resp.put(authenticatorSource);
            sharedSecret = "slbota";
            bb_resp.put(sharedSecret.getBytes());
            bb_resp.flip();
            byte[] arr_resp = new byte[bb_resp.limit()];
            bb_resp.get(arr_resp);
            byte[] authenticatorISMG = md.digest(arr_resp);
            logger.debug("authenticatorISMG: " + ByteUtil.toHexForLog(authenticatorISMG));
        } catch (NoSuchAlgorithmException e) {
            logger.debug("I don't know how to compute MD5!");
            System.exit(1);
        }
    }
