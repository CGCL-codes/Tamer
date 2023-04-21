    public static DefaultWriteAction read(IFileSystemInterface fsi, int index) {
        try {
            long position = fsi.readLong();
            int size = fsi.readInt();
            byte[] bytes = fsi.readBytes(size);
            DefaultWriteAction writeAction = new DefaultWriteAction(position, bytes);
            if (OdbConfiguration.isDebugEnabled(LOG_ID)) {
                DLogger.debug("Loading Write Action # " + index + " at " + fsi.getPosition() + " => " + writeAction.toString());
            }
            return writeAction;
        } catch (ODBRuntimeException e) {
            DLogger.error("error reading write action " + index + " at position " + fsi.getPosition());
            throw e;
        }
    }
