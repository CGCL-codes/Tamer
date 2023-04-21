    public static void bytesToLittleEndian(byte[] src) {
        LOG.enterMethod("Util.bytesToLittleEndian(byte[])");
        byte temp;
        if ((src.length == 0) || ((src.length % 4) != 0)) {
            String msg;
            msg = I18nHelper.getInstance().formatMessage("E00009", new Object[] { new Integer(src.length) });
            LOG.error(msg);
            LOG.leaveMethod("Util.bytesToLittleEndian(byte[]) - by throwing IllegalArgumentException");
            throw new IllegalArgumentException(msg);
        }
        if (LOG.isDebug2Enabled()) LOG.debug2("Bytes to be swapped = " + bytesToString(src));
        for (int ii = 0; ii < src.length; ii += 4) {
            temp = src[ii];
            src[ii] = src[ii + 3];
            src[ii + 3] = temp;
            temp = src[ii + 1];
            src[ii + 1] = src[ii + 2];
            src[ii + 2] = temp;
        }
        if (LOG.isDebug2Enabled()) LOG.debug2("Bytes after swapping = " + bytesToString(src));
        LOG.leaveMethod("Util.bytesToLittleEndian(byte[])");
    }
