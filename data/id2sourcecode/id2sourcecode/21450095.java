    private void writePackets(Thread thisThread) {
        try {
            openStream();
            while (!done && (writerThread == thisThread)) {
                Packet packet = nextPacket();
                if (packet != null) {
                    synchronized (writer) {
                        writer.write(packet.toXML());
                        writer.flush();
                        lastActive = System.currentTimeMillis();
                    }
                }
            }
            try {
                synchronized (writer) {
                    while (!queue.isEmpty()) {
                        Packet packet = queue.remove();
                        writer.write(packet.toXML());
                    }
                    writer.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            queue.clear();
            try {
                writer.write("</stream:stream>");
                writer.flush();
            } catch (Exception e) {
            } finally {
                try {
                    writer.close();
                } catch (Exception e) {
                }
            }
        } catch (IOException ioe) {
            if (!done) {
                done = true;
                connection.packetReader.notifyConnectionError(ioe);
            }
        }
    }
