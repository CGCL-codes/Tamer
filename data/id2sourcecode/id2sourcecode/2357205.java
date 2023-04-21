    private void sendConnect(String privateName) throws SpreadException {
        int len = (privateName == null ? 0 : privateName.length());
        if (len > MAX_PRIVATE_NAME) {
            privateName = privateName.substring(0, MAX_PRIVATE_NAME);
            len = MAX_PRIVATE_NAME;
        }
        byte buffer[] = new byte[len + 5];
        buffer[0] = (byte) SP_MAJOR_VERSION;
        buffer[1] = (byte) SP_MINOR_VERSION;
        buffer[2] = (byte) SP_PATCH_VERSION;
        buffer[3] = 0;
        if (groupMembership) {
            buffer[3] |= 0x01;
        }
        if (priority) {
            buffer[3] |= 0x10;
        }
        buffer[4] = (byte) len;
        if (len > 0) {
            byte nameBytes[] = privateName.getBytes();
            for (int src = 0, dest = 5; src < len; src++, dest++) {
                buffer[dest] = nameBytes[src];
            }
        }
        try {
            socketOutput.write(buffer);
        } catch (IOException e) {
            throw new SpreadException("write(): " + e);
        }
    }
