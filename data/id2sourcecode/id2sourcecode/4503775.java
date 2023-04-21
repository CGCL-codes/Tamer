    public byte[] readMessage() throws SocketException, IOException {
        message.reset();
        read = readBufferedData(initial, 0, cipherlen);
        cipher = algorithms.getCipher();
        hmac = algorithms.getHmac();
        compression = algorithms.getCompression();
        if (cipher != null) {
            cipherlen = cipher.getBlockSize();
        } else {
            cipherlen = 8;
        }
        if (initial.length != cipherlen) {
            byte[] tmp = new byte[cipherlen];
            System.arraycopy(initial, 0, tmp, 0, initial.length);
            initial = tmp;
        }
        int count = read;
        if (count < initial.length) {
            count += readBufferedData(initial, count, initial.length - count);
        }
        if (hmac != null) {
            maclen = hmac.getMacLength();
        } else {
            maclen = 0;
        }
        if (cipher != null) {
            initial = cipher.transform(initial);
        }
        message.write(initial);
        msglen = (int) ByteArrayReader.readInt(initial, 0);
        padlen = initial[4];
        remaining = (msglen - (cipherlen - 4));
        while (remaining > 0) {
            read = readBufferedData(data, 0, (remaining < data.length) ? ((remaining / cipherlen) * cipherlen) : ((data.length / cipherlen) * cipherlen));
            remaining -= read;
            message.write((cipher == null) ? data : cipher.transform(data, 0, read), 0, read);
        }
        synchronized (sequenceLock) {
            if (hmac != null) {
                read = readBufferedData(data, 0, maclen);
                message.write(data, 0, read);
                if (!hmac.verify(sequenceNo, message.toByteArray())) {
                    throw new IOException("Corrupt Mac on input");
                }
            }
            if (sequenceNo < sequenceWrapLimit) {
                sequenceNo++;
            } else {
                sequenceNo = 0;
            }
        }
        bytesTransfered += message.size();
        byte[] msg = message.toByteArray();
        if (compression != null) {
            return compression.uncompress(msg, 5, (msglen + 4) - padlen - 5);
        }
        return msg;
    }
