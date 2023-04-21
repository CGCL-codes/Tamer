    public void run() {
        if (LOG.isTraceEnabled()) LOG.trace("Starting TCPServerThread");
        ByteArrayOutputStream dataBuffer = new ByteArrayOutputStream();
        try {
            boolean goodPacket = true;
            while (true) {
                if (isInterrupted()) break;
                byte[] readBuffer = new byte[1024];
                byte[] readBufferIntermediate;
                int read = this.inputStream.read(readBuffer);
                if (read == -1) {
                    this.inputStream.close();
                    this.socket.close();
                    break;
                }
                readBufferIntermediate = new byte[read];
                System.arraycopy(readBuffer, 0, readBufferIntermediate, 0, read);
                dataBuffer.write(readBufferIntermediate);
                if (dataBuffer.size() > ProtocolWorker.PACKET_SIZE_MAX) {
                    goodPacket = false;
                    break;
                }
            }
            dataBuffer.close();
            if (goodPacket) {
                int secondsPassed = 0;
                while (this.tcpServer.getBufferCapacity() == 0) {
                    sleep(1000);
                    if (++secondsPassed == 10) {
                        LOG.warn("After 10 seconds buffer is still full. Dropping packet from " + this.socket.getInetAddress());
                        break;
                    }
                }
                this.tcpServer.receivePacket(new TransportPacket(this.socket.getInetAddress(), this.socket.getPort(), dataBuffer.toByteArray()));
            } else {
                LOG.warn("Received packet of size excessing 64kB");
            }
            this.inputStream.close();
        } catch (Throwable e) {
            LOG.error("Error while reading packet", e);
        } finally {
            if (this.socket != null) {
                try {
                    this.socket.close();
                } catch (IOException e) {
                    LOG.error("Error while closing the socket", e);
                }
            }
        }
    }
