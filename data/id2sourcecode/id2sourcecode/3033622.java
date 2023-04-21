    static void readResponse(ServerConnection sc, BufferSet bufferSet, long timeout, byte[] stop) throws IOException {
        boolean done = false;
        ByteBuffer buf = bufferSet.allocateBuffer();
        do {
            int keyCount = sc.getSelector().select(timeout);
            if (keyCount == 0) {
                sc.closeConnection();
                break;
            }
            Iterator<SelectionKey> it = sc.getSelector().selectedKeys().iterator();
            it.hasNext();
            it.next();
            it.remove();
            sc.getChannel().read(buf);
            if (buf.position() >= stop.length) {
                int checkStart = buf.position() - stop.length;
                int I;
                for (I = 0; I < stop.length; I++) {
                    byte b = buf.get(I + checkStart);
                    if (b != stop[I]) {
                        break;
                    }
                }
                if (I == stop.length) done = true;
            }
            if ((!done) && (buf.position() == buf.limit())) {
                buf.flip();
                buf = bufferSet.allocateBuffer();
            }
        } while (!done);
        buf.flip();
    }
