    public void run() {
        if (_socket != null) {
            MSG_PORT_OPEN open_packet = new MSG_PORT_OPEN(_localChannelNumber, _hostname, _remoteport);
            _handler.enqueueToRemote(open_packet);
            Packet reply = _queue.getNextPacket();
            if (reply.getPacketType() != SSH_MSG_CHANNEL_OPEN_CONFIRMATION) {
                try {
                    _socket.close();
                } catch (IOException e) {
                }
                return;
            }
            MSG_CHANNEL_OPEN_CONFIRMATION confirm = (MSG_CHANNEL_OPEN_CONFIRMATION) reply;
            _remoteChannelNumber = confirm.getLocalChannelNumber();
        } else {
            try {
                InetAddress address = InetAddress.getByName(_hostname);
                _socket = new Socket(address, _localport);
            } catch (IOException e) {
                MSG_CHANNEL_OPEN_FAILURE reply = new MSG_CHANNEL_OPEN_FAILURE(_remoteChannelNumber);
                _handler.enqueueToRemote(reply);
                return;
            }
            _handler.registerOpenChannel(this);
            MSG_CHANNEL_OPEN_CONFIRMATION reply = new MSG_CHANNEL_OPEN_CONFIRMATION(_remoteChannelNumber, getLocalChannelNumber());
            _handler.enqueueToRemote(reply);
        }
        try {
            ChannelReader channel_reader = new ChannelReader(_socket.getInputStream());
            channel_reader.setDaemon(false);
            channel_reader.start();
            OutputStream out = _socket.getOutputStream();
            for (; ; ) {
                Packet packet = _queue.getNextPacket();
                int type = packet.getPacketType();
                if (type == SSH_MSG_CHANNEL_DATA) {
                    MSG_CHANNEL_DATA data_packet = (MSG_CHANNEL_DATA) packet;
                    out.write(data_packet.getChannelData());
                } else if (type == SSH_MSG_CHANNEL_CLOSE) {
                    _socket.shutdownOutput();
                    _socket.close();
                    MSG_CHANNEL_CLOSE_CONFIRMATION confirmation = new MSG_CHANNEL_CLOSE_CONFIRMATION(_remoteChannelNumber);
                    _handler.enqueueToRemote(confirmation);
                    break;
                } else break;
            }
        } catch (IOException e) {
            MSG_CHANNEL_CLOSE close_packet = new MSG_CHANNEL_CLOSE(_remoteChannelNumber);
            _handler.enqueueToRemote(close_packet);
            try {
                _socket.close();
            } catch (Exception e2) {
            }
        }
        _handler.removeOpenChannel(this);
        return;
    }
