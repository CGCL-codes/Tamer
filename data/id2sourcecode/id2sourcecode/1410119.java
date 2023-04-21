    ChannelPacket readPacket() throws IOException {
        ChannelPacket packet = null;
        long time = System.currentTimeMillis();
        if (readPos < firstPos) {
            throw new IOException("Read too far behind");
        }
        while (readPos >= writePos) {
            try {
                Thread.sleep(Peercast.idleSleepTime);
            } catch (InterruptedException e) {
            }
            if ((System.currentTimeMillis() - time) > 30 * 1000) {
                throw new IOException("timeout");
            }
        }
        synchronized (this) {
            packet = packets.get(readPos % MAX_PACKETS);
            readPos++;
        }
        try {
            Thread.sleep(Peercast.idleSleepTime);
        } catch (InterruptedException e) {
        }
        return packet;
    }
