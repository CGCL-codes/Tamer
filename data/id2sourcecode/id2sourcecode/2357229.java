    public void multicast(SpreadMessage message) throws SpreadException {
        if (connected == false) {
            throw new SpreadException("Not connected.");
        }
        SpreadGroup groups[] = message.getGroups();
        byte data[] = message.getData();
        int numBytes = 16;
        numBytes += MAX_GROUP_NAME;
        numBytes += (MAX_GROUP_NAME * groups.length);
        if (numBytes + data.length > MAX_MESSAGE_LENGTH) {
            throw new SpreadException("Message is too long for a Spread Message");
        }
        byte buffer[] = new byte[numBytes];
        int bufferIndex = 0;
        toBytes(message.getServiceType(), buffer, bufferIndex);
        bufferIndex += 4;
        toBytes(group, buffer, bufferIndex);
        bufferIndex += MAX_GROUP_NAME;
        toBytes(groups.length, buffer, bufferIndex);
        bufferIndex += 4;
        toBytes(((int) message.getType() << 8) & 0x00FFFF00, buffer, bufferIndex);
        bufferIndex += 4;
        toBytes(data.length, buffer, bufferIndex);
        bufferIndex += 4;
        for (int i = 0; i < groups.length; i++) {
            toBytes(groups[i], buffer, bufferIndex);
            bufferIndex += MAX_GROUP_NAME;
        }
        synchronized (wsynchro) {
            try {
                socketOutput.write(buffer);
                socketOutput.write(data);
            } catch (IOException e) {
                throw new SpreadException("write(): " + e.toString());
            }
        }
    }
