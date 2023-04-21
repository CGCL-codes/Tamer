    private static String getFingerPrint(Certificate c) {
        if (Logging.SHOW_INFO && LOG.isLoggable(Level.INFO)) {
            LOG.info("getFingerPrint");
        }
        StringBuffer sb = null;
        byte[] ba = null;
        try {
            ba = MessageDigest.getInstance(ALGORITHM).digest(c.getEncoded());
        } catch (Exception e) {
            if (Logging.SHOW_INFO && LOG.isLoggable(Level.INFO)) {
                LOG.log(Level.INFO, "can\'t get messsage digest", e);
            }
        }
        if (ba != null) {
            sb = new StringBuffer();
            byte b;
            for (int i = 0; i < ba.length; i++) {
                b = ba[i];
                sb.append(CHAR_MAP[(b & 0xf0) >> 4]);
                sb.append(CHAR_MAP[b & 0xf]);
                if (i < ba.length - 1) {
                    sb.append(":");
                }
            }
        }
        String fp = sb != null ? sb.toString() : null;
        if (Logging.SHOW_INFO && LOG.isLoggable(Level.INFO)) {
            LOG.info("fingerPrint: " + fp);
        }
        return fp;
    }
