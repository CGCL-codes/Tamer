    public void decrypt(ChannelBuffer buffer, int nLen) {
        ChannelBuffer mirror = buffer.duplicate();
        mirror.writerIndex(buffer.readerIndex());
        try {
            long lTemp;
            while (nLen >= 8) {
                lTemp = buffer.readLong();
                lTemp = cipherBlockCBC(lTemp);
                mirror.writeLong(lTemp);
                nLen -= 8;
            }
            if (nLen > 0) {
                long lastLong = 0l;
                for (int pos = 0; pos < nLen; pos++) {
                    byte temp = buffer.readByte();
                    lastLong = (lastLong << 8) | (temp & 0xFF);
                }
                for (int c = 0; c < (8 - nLen); c++) {
                    lastLong = (lastLong << 8) | 0x20;
                }
                lastLong = cipherBlockCBC(lastLong);
                for (int a = 0; a < nLen; a++) {
                    byte temp = (byte) ((lastLong & 0xFF00000000000000l) >> 56);
                    mirror.writeByte(temp);
                    lastLong <<= 8;
                }
            }
        } catch (IndexOutOfBoundsException e) {
            if (buffer.readableBytes() < nLen) {
                MUSLog.Log("Decrypt ERROR: Not enough bytes in buffer " + buffer.readableBytes() + "/" + nLen, MUSLog.kDeb);
            }
            long lastLong = 0l;
            for (int pos = 0; pos < nLen; pos++) {
                byte temp = buffer.readByte();
                lastLong = (lastLong << 8) | (temp & 0xFF);
            }
            for (int c = 0; c < (8 - nLen); c++) {
                lastLong = (lastLong << 8) | 0x20;
            }
            lastLong = cipherBlockCBC(lastLong);
            for (int a = 0; a < nLen; a++) {
                byte temp = (byte) ((lastLong & 0xFF00000000000000l) >> 56);
                mirror.writeByte(temp);
                lastLong <<= 8;
            }
        }
    }
