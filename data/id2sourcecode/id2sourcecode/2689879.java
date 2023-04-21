    public static byte[] fileToByteArray(File file) {
        if (logger.isDebugEnabled()) {
            logger.debug("fileToByteArray() - start");
        }
        byte buffer[] = null;
        try {
            FileInputStream in = new FileInputStream(file);
            int fl = (int) file.length();
            buffer = new byte[fl];
            @SuppressWarnings("unused") int len = in.read(buffer, 0, fl);
            if (logger.isDebugEnabled()) {
                logger.debug("fileToByteArray() - file '" + file.getName() + "' transformed to byte array");
            }
        } catch (Exception e) {
            logger.error("Error: Can not transform file '" + file.getName() + "' to byte array", e);
        }
        if (logger.isDebugEnabled()) {
            logger.debug("fileToByteArray() - end");
        }
        return buffer;
    }
