    public final void sendMessage(Message message) {
        try {
            if (remoteClient.getCompressionMode() == RemoteClient.COMPRESSION_GZIP) {
                dout.writeByte(Protocol.ENC_COMPRESSED_GZIP);
                dout.writeInt(messageID++);
                ByteArrayOutputStream gzBuffer = new ByteArrayOutputStream();
                GZIPOutputStream gzout = new GZIPOutputStream(gzBuffer);
                gzout.write(message.getMessageBytes());
                gzout.close();
                gzBuffer.close();
                dout.writeInt(gzBuffer.toByteArray().length);
                dout.writeByte(message.getMessageFlag());
                dout.writeInt(message.getMessageLength());
                dout.write(gzBuffer.toByteArray());
                sleep(1L);
            } else if (remoteClient.getCompressionMode() == RemoteClient.COMPRESSION_LZIP) {
                dout.writeByte(Protocol.ENC_COMPRESSED_LZIP);
                dout.writeInt(messageID++);
                ByteArrayOutputStream lzBuffer = new ByteArrayOutputStream();
                DeflaterOutputStream lzout = new DeflaterOutputStream(lzBuffer);
                lzout.write(message.getMessageBytes());
                lzout.close();
                lzBuffer.close();
                dout.writeInt(lzBuffer.size());
                dout.writeByte(message.getMessageFlag());
                dout.writeInt(message.getMessageLength());
                dout.write(lzBuffer.toByteArray());
                sleep(1L);
            } else {
                dout.writeByte(message.getMessageFlag());
                dout.writeInt(messageID++);
                dout.writeInt(message.getMessageLength());
                dout.write(message.getMessageBytes());
            }
            dout.flush();
        } catch (IOException e) {
            logManager.writeToLog(1, "NET", "[" + threadID + "] Socket write error: " + e.getMessage());
        }
    }
