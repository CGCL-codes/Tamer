    private static long write(FileChannel fileChannelIn, FileChannel fileChannelOut) throws OpenR66ProtocolSystemException {
        if (fileChannelIn == null) {
            if (fileChannelOut != null) {
                try {
                    fileChannelOut.close();
                } catch (IOException e) {
                }
            }
            throw new OpenR66ProtocolSystemException("FileChannelIn is null");
        }
        if (fileChannelOut == null) {
            try {
                fileChannelIn.close();
            } catch (IOException e) {
            }
            throw new OpenR66ProtocolSystemException("FileChannelOut is null");
        }
        long size = 0;
        long transfert = 0;
        try {
            long position = fileChannelOut.position();
            size = fileChannelIn.size();
            transfert = fileChannelOut.transferFrom(fileChannelIn, position, size);
        } catch (IOException e) {
            try {
                fileChannelOut.close();
                fileChannelIn.close();
            } catch (IOException e1) {
            }
            throw new OpenR66ProtocolSystemException("An error during copy occurs", e);
        }
        try {
            fileChannelOut.close();
            fileChannelIn.close();
        } catch (IOException e) {
        }
        boolean retour = size == transfert;
        if (!retour) {
            throw new OpenR66ProtocolSystemException("Copy is not complete: " + transfert + " bytes instead of " + size + " original bytes");
        }
        return size;
    }
