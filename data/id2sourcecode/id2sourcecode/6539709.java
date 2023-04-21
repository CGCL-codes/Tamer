        public ItemV3(PwsFileV3 file) throws EndOfFileException, IOException {
            super();
            try {
                rawData = file.readBlock();
            } catch (EndOfFileException eofe) {
                data = new byte[32];
                file.readBytes(data);
                byte[] hash = file.hasher.doFinal();
                if (!Util.bytesAreEqual(data, hash)) {
                    LOG.error("HMAC record did not match. File may have been tampered");
                    throw new IOException("HMAC record did not match. File has been tampered");
                }
                throw eofe;
            }
            length = Util.getIntFromByteArray(rawData, 0);
            type = rawData[4] & 0x000000ff;
            data = new byte[length];
            byte[] remainingDataInRecord = Util.getBytes(rawData, 5, 11);
            if (length <= 11) {
                Util.copyBytes(Util.getBytes(remainingDataInRecord, 0, length), data);
            } else if (length > 11) {
                int bytesToRead = length - 11;
                int blocksToRead = bytesToRead / file.getBlockSize();
                if (bytesToRead % file.getBlockSize() != 0) blocksToRead++;
                byte[] remainingRecords = new byte[0];
                for (int i = 0; i < blocksToRead; i++) {
                    byte[] nextBlock = new byte[file.getBlockSize()];
                    file.readDecryptedBytes(nextBlock);
                    if (i == blocksToRead - 1) {
                        nextBlock = Util.getBytes(nextBlock, 0, bytesToRead - remainingRecords.length);
                    }
                    remainingRecords = Util.mergeBytes(remainingRecords, nextBlock);
                }
                data = Util.mergeBytes(remainingDataInRecord, remainingRecords);
            }
            byte[] dataToHash = data;
            file.hasher.digest(dataToHash);
        }
