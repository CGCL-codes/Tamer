    protected boolean get(FileChannel fileChannelOut) {
        if (!isReady) {
            return false;
        }
        FileChannel fileChannelIn = getFileChannel();
        if (fileChannelIn == null) {
            return false;
        }
        long size = 0;
        long transfert = 0;
        try {
            size = fileChannelIn.size();
            transfert = fileChannelOut.transferFrom(fileChannelIn, 0, size);
            fileChannelOut.force(true);
            fileChannelIn.close();
            fileChannelIn = null;
            fileChannelOut.close();
        } catch (IOException e) {
            logger.error("Error during get:", e);
            if (fileChannelIn != null) {
                try {
                    fileChannelIn.close();
                } catch (IOException e1) {
                }
            }
            return false;
        }
        if (transfert == size) {
            position += size;
        }
        return transfert == size;
    }
