        public ItemV3(PwsFileV3 file) throws EndOfFileException, IOException {
            super();
            try {
                RawData = file.readBlock();
            } catch (EndOfFileException eofe) {
                Data = new byte[32];
                file.readBytes(Data);
                byte[] hash = file.hasher.doFinal();
                if (!Util.bytesAreEqual(Data, hash)) {
                    LOG.error("HMAC record did not match. File may have been tampered");
                    throw new IOException("HMAC record did not match. File has been tampered");
                }
                throw eofe;
            }
            Length = Util.getIntFromByteArray(RawData, 0);
            Type = RawData[4] & 0x000000ff;
            Data = new byte[Length];
            byte[] remainingDataInRecord = Util.getBytes(RawData, 5, 11);
            if (Length <= 11) {
                Util.copyBytes(Util.getBytes(remainingDataInRecord, 0, Length), Data);
            } else if (Length > 11) {
                int bytesToRead = Length - 11;
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
                Data = Util.mergeBytes(remainingDataInRecord, remainingRecords);
            }
            byte[] dataToHash = Data;
            file.hasher.digest(dataToHash);
        }
