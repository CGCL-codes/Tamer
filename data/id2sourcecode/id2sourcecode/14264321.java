    public void handle(Packet p) {
        LOG.debug("Got packet");
        p.setSerial(nextSerial++);
        int ch_id = p.getChannelID();
        SocketChannel ch = null;
        if (0 >= ch_id) {
            InetAddress ia = p.getIP();
            int port = p.getPort();
            if (null == ia) {
                LOG.error("Unable to send packet " + p.getSerial() + ":  channel ID not set");
            } else {
                try {
                    SocketChannel newChan = SocketChannel.open();
                    newChan.connect(new InetSocketAddress(ia, port));
                    channelListPort.send(getContext().createMessage(new AddChannelCommand(newChan)));
                    writePacket(p, newChan);
                } catch (IOException e) {
                    LOG.error("Unable to send packet", e);
                } catch (PortException e) {
                    LOG.error("Unable to send add-channel command", e);
                }
            }
        } else {
            Object key = new Integer(ch_id);
            ch = (SocketChannel) channels.get(key);
            if (null == ch) {
                Collection list = (Collection) pending.get(key);
                if (null == list) {
                    list = new Vector();
                    pending.put(key, list);
                }
                list.add(p);
                try {
                    channelListPort.send(getContext().createMessage(new GetChannelCommand(ch_id)));
                } catch (PortException e) {
                    LOG.error("Unable to send get-channel command", e);
                }
            } else {
                writePacket(p, ch);
            }
        }
    }
