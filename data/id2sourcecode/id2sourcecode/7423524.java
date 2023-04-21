    protected PIFData getPifData(TarEntryHeader header) throws IOException, TarMalformatException {
        long dataSize = header.getDataSize();
        if (dataSize < 1) {
            throw new TarMalformatException(RB.pif_unknown_datasize.getString());
        }
        if (dataSize > Integer.MAX_VALUE) {
            throw new TarMalformatException(RB.pif_data_toobig.getString(Long.toString(dataSize), Integer.MAX_VALUE));
        }
        int readNow;
        int readBlocks = (int) (dataSize / 512L);
        int modulus = (int) (dataSize % 512L);
        PipedInputStream inPipe = null;
        PipedOutputStream outPipe = new PipedOutputStream();
        try {
            inPipe = new PipedInputStream(outPipe);
            while (readBlocks > 0) {
                readNow = (readBlocks > archive.getReadBufferBlocks()) ? archive.getReadBufferBlocks() : readBlocks;
                archive.readBlocks(readNow);
                readBlocks -= readNow;
                outPipe.write(archive.readBuffer, 0, readNow * 512);
            }
            if (modulus != 0) {
                archive.readBlock();
                outPipe.write(archive.readBuffer, 0, modulus);
            }
            outPipe.flush();
        } catch (IOException ioe) {
            if (inPipe != null) {
                inPipe.close();
            }
            throw ioe;
        } finally {
            try {
                outPipe.close();
            } finally {
                outPipe = null;
                inPipe = null;
            }
        }
        return new PIFData(inPipe);
    }
