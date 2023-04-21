    private DataList readRemainingBytes(String messageType) throws Exception {
        DataList data = new DataList();
        data.numBytes = Integer.parseInt(recieve(Constants.MESSAGE_WAIT_TIME));
        in.readFully(writeBuf, 0, data.numBytes);
        long checksum = 0;
        byte prev = 1;
        for (int i = 0; i < data.numBytes; i++) {
            checksum += writeBuf[i] + (writeBuf[i] - prev) * (writeBuf[i] - prev);
            prev = writeBuf[i];
        }
        if (checksum != in.readLong()) {
            hnd.LogMessage(3, "corrupt packet, retrying");
            return null;
        }
        data.data = writeBuf;
        return data;
    }
