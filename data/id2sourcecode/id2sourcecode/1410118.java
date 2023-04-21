    boolean writePacket(ChannelPacket pack, boolean updateReadPos) {
        if (pack.data.length != 0) {
            if (willSkip()) {
                return false;
            }
            synchronized (this) {
                pack.sync = writePos;
                packets.add(writePos, pack);
                lastPos = writePos;
                writePos++;
                if (writePos >= MAX_PACKETS) {
                    firstPos = writePos - MAX_PACKETS;
                } else {
                    firstPos = 0;
                }
                if (writePos >= NUM_SAFEPACKETS) {
                    safePos = writePos - NUM_SAFEPACKETS;
                } else {
                    safePos = 0;
                }
                if (updateReadPos) {
                    readPos = writePos;
                }
                lastWriteTime = System.currentTimeMillis();
            }
            return true;
        }
        return false;
    }
