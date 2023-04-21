    public void run() {
        while (started) {
            synchronized (this.newChannels) {
                try {
                    for (RtpSocketImpl rtpSocketImpl : this.newChannels) {
                        SelectionKey key = rtpSocketImpl.getChannel().register(this.readSelector, SelectionKey.OP_READ);
                        rtpSocketImpl.setSelectionKey(key);
                        this.rtpSockets.put(key, rtpSocketImpl);
                    }
                } catch (ClosedChannelException e) {
                    e.printStackTrace();
                }
                this.newChannels.clear();
            }
            try {
                readSelector.select(2);
                long receiveStamp = System.currentTimeMillis();
                Set<SelectionKey> keys = readSelector.selectedKeys();
                for (SelectionKey sk : keys) {
                    try {
                        RtpSocket socket = this.rtpSockets.get(sk);
                        if (socket == null) {
                            continue;
                        }
                        DatagramChannel channel = socket.getChannel();
                        if (channel == null || !socket.isChannelOpen() || (channel != null && !channel.isConnected())) {
                            continue;
                        }
                        int i = channel.read(readerBuffer);
                        readerBuffer.flip();
                        if (i > 0) {
                            RtpPacket rtpPacket = new RtpPacket(readerBuffer);
                            rtpPacket.setTime(new Date(receiveStamp));
                            socket.receive(rtpPacket);
                        }
                        readerBuffer.clear();
                    } catch (IOException e) {
                        if (started) {
                            notifyError(e);
                        }
                    }
                }
            } catch (IOException e1) {
                e1.printStackTrace();
            }
        }
    }
