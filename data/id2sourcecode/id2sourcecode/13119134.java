    public Boolean call() throws XMLStreamException {
        byte[] byteRead;
        int nRead;
        String strChunk;
        logger.debug("Attaching input data from input stream");
        if (bLogIS) {
            initLogger();
        }
        byteRead = new byte[bytes4Chunck];
        nRead = 0;
        while (nRead >= 0) {
            try {
                nRead = input.read(byteRead);
                readedBytes += nRead;
                if (nRead != bytes4Chunck) {
                    byte[] byteFit = new byte[nRead];
                    System.arraycopy(byteRead, 0, byteFit, 0, nRead);
                    byteRead = byteFit;
                }
                if (nRead > 0) {
                    writer.writeBinary(byteRead, 0, nRead);
                    writer.flush();
                    if (bLogIS) {
                        logEncodedChunk(strChunk);
                    }
                }
            } catch (IOException e) {
                String msg = "Error reading input stream";
                logger.error(msg, e);
                throw new XMLStreamException(msg, e);
            }
            logger.debug(String.format("Read->write: %d (raw bytes) ", readedBytes));
        }
        if (isClossingTransferResponse) {
            finishWrite();
        }
        logger.debug("All the content was written");
        if (bLogIS) {
            try {
                writerOUTLOG.close();
            } catch (XMLStreamException e) {
            }
        }
        return Boolean.TRUE;
    }
