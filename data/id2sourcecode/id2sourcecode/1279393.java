    public final Message readMessage() throws IOException {
        byte messageFlag = din.readByte();
        int messageID = din.readInt();
        int messageLength = din.readInt();
        byte[] inBuffer = null;
        if (messageFlag == Protocol.ENC_COMPRESSED_GZIP) {
            messageFlag = din.readByte();
            int decompressedLength = din.readInt();
            GZIPInputStream gzin = new GZIPInputStream(din);
            int readLength = decompressedLength;
            if (messageLength > decompressedLength) {
                readLength = messageLength;
            }
            byte[] gzBuffer = new byte[readLength];
            gzin.read(gzBuffer, 0, readLength);
            inBuffer = new byte[decompressedLength];
            System.arraycopy(gzBuffer, 0, inBuffer, 0, decompressedLength);
            logManager.writeToLog(6, "NET", "[" + threadID + "] Compression rate: " + decompressedLength + " -> " + messageLength);
        } else if (messageFlag == Protocol.ENC_COMPRESSED_LZIP) {
            messageFlag = din.readByte();
            int decompressedLength = din.readInt();
            InflaterInputStream lzin = new InflaterInputStream(din);
            int readLength = decompressedLength;
            if (messageLength > decompressedLength) {
                readLength = messageLength;
            }
            byte[] lzBuffer = new byte[readLength];
            lzin.read(lzBuffer, 0, readLength);
            inBuffer = new byte[decompressedLength];
            System.arraycopy(lzBuffer, 0, inBuffer, 0, decompressedLength);
            logManager.writeToLog(6, "NET", "[" + threadID + "] Compression rate: " + decompressedLength + " -> " + messageLength);
        } else {
            inBuffer = new byte[messageLength];
            din.read(inBuffer, 0, messageLength);
        }
        return new Message(messageFlag, messageID, messageLength, inBuffer);
    }
