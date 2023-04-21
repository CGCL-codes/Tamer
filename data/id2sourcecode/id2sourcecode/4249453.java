    protected void writePackets(Thread thisThread) {
        try {
            writeStart();
            while ((writerThread == thisThread)) {
                Packet packet = nextPacket();
                if (packet != null) {
                    synchronized (eventWriter) {
                        eventWriter.add(packet.getEventReader());
                        eventWriter.flush();
                    }
                }
            }
            try {
                synchronized (eventWriter) {
                    while (!queue.isEmpty()) {
                        Packet packet = queue.remove();
                        eventWriter.add(packet.getEventReader());
                    }
                    eventWriter.flush();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
            queue.clear();
            try {
                XMLEventFactory xmlEventFactory = XMLEventFactory.newInstance();
                eventWriter.add(xmlEventFactory.createEndElement("stream", "http://etherx.jabber.org/streams", "stream"));
                eventWriter.flush();
            } catch (Exception e) {
            } finally {
                try {
                    eventWriter.close();
                } catch (Exception e) {
                }
            }
        } catch (XMPPWriteException ioe) {
        } catch (XMLStreamException e) {
            e.printStackTrace();
        } catch (PacketException e) {
            e.printStackTrace();
        }
    }
