    public void run() {
        thread = this;
        byte[] foo;
        Buffer buf = new Buffer();
        Packet packet = new Packet(buf);
        int i = 0;
        Channel channel;
        int[] start = new int[1];
        int[] length = new int[1];
        KeyExchange kex = null;
        int stimeout = 0;
        try {
            while (isConnected && thread != null) {
                try {
                    buf = read(buf);
                    stimeout = 0;
                } catch (InterruptedIOException ee) {
                    if (!in_kex && stimeout < serverAliveCountMax) {
                        sendKeepAliveMsg();
                        stimeout++;
                        continue;
                    }
                    throw ee;
                }
                int msgType = buf.getCommand() & 0xff;
                if (kex != null && kex.getState() == msgType) {
                    boolean result = kex.next(buf);
                    if (!result) {
                        throw new JSchException("verify: " + result);
                    }
                    continue;
                }
                switch(msgType) {
                    case SSH_MSG_KEXINIT:
                        kex = receive_kexinit(buf);
                        break;
                    case SSH_MSG_NEWKEYS:
                        send_newkeys();
                        receive_newkeys(buf, kex);
                        kex = null;
                        break;
                    case SSH_MSG_CHANNEL_DATA:
                        buf.getInt();
                        buf.getByte();
                        buf.getByte();
                        i = buf.getInt();
                        channel = Channel.getChannel(i, this);
                        foo = buf.getString(start, length);
                        if (channel == null) {
                            break;
                        }
                        if (length[0] == 0) {
                            break;
                        }
                        try {
                            channel.write(foo, start[0], length[0]);
                        } catch (Exception e) {
                            try {
                                channel.disconnect();
                            } catch (Exception ee) {
                            }
                            break;
                        }
                        int len = length[0];
                        channel.setLocalWindowSize(channel.lwsize - len);
                        if (channel.lwsize < channel.lwsize_max / 2) {
                            packet.reset();
                            buf.putByte((byte) SSH_MSG_CHANNEL_WINDOW_ADJUST);
                            buf.putInt(channel.getRecipient());
                            buf.putInt(channel.lwsize_max - channel.lwsize);
                            write(packet);
                            channel.setLocalWindowSize(channel.lwsize_max);
                        }
                        break;
                    case SSH_MSG_CHANNEL_EXTENDED_DATA:
                        buf.getInt();
                        buf.getShort();
                        i = buf.getInt();
                        channel = Channel.getChannel(i, this);
                        buf.getInt();
                        foo = buf.getString(start, length);
                        if (channel == null) {
                            break;
                        }
                        if (length[0] == 0) {
                            break;
                        }
                        channel.write_ext(foo, start[0], length[0]);
                        len = length[0];
                        channel.setLocalWindowSize(channel.lwsize - len);
                        if (channel.lwsize < channel.lwsize_max / 2) {
                            packet.reset();
                            buf.putByte((byte) SSH_MSG_CHANNEL_WINDOW_ADJUST);
                            buf.putInt(channel.getRecipient());
                            buf.putInt(channel.lwsize_max - channel.lwsize);
                            write(packet);
                            channel.setLocalWindowSize(channel.lwsize_max);
                        }
                        break;
                    case SSH_MSG_CHANNEL_WINDOW_ADJUST:
                        buf.getInt();
                        buf.getShort();
                        i = buf.getInt();
                        channel = Channel.getChannel(i, this);
                        if (channel == null) {
                            break;
                        }
                        channel.addRemoteWindowSize(buf.getInt());
                        break;
                    case SSH_MSG_CHANNEL_EOF:
                        buf.getInt();
                        buf.getShort();
                        i = buf.getInt();
                        channel = Channel.getChannel(i, this);
                        if (channel != null) {
                            channel.eof_remote();
                        }
                        break;
                    case SSH_MSG_CHANNEL_CLOSE:
                        buf.getInt();
                        buf.getShort();
                        i = buf.getInt();
                        channel = Channel.getChannel(i, this);
                        if (channel != null) {
                            channel.disconnect();
                        }
                        break;
                    case SSH_MSG_CHANNEL_OPEN_CONFIRMATION:
                        buf.getInt();
                        buf.getShort();
                        i = buf.getInt();
                        channel = Channel.getChannel(i, this);
                        if (channel == null) {
                        }
                        int r = buf.getInt();
                        int rws = buf.getInt();
                        int rps = buf.getInt();
                        channel.setRemoteWindowSize(rws);
                        channel.setRemotePacketSize(rps);
                        channel.setRecipient(r);
                        break;
                    case SSH_MSG_CHANNEL_OPEN_FAILURE:
                        buf.getInt();
                        buf.getShort();
                        i = buf.getInt();
                        channel = Channel.getChannel(i, this);
                        if (channel == null) {
                        }
                        int reason_code = buf.getInt();
                        channel.exitstatus = reason_code;
                        channel.close = true;
                        channel.eof_remote = true;
                        channel.setRecipient(0);
                        break;
                    case SSH_MSG_CHANNEL_REQUEST:
                        buf.getInt();
                        buf.getShort();
                        i = buf.getInt();
                        foo = buf.getString();
                        boolean reply = (buf.getByte() != 0);
                        channel = Channel.getChannel(i, this);
                        if (channel != null) {
                            byte reply_type = (byte) SSH_MSG_CHANNEL_FAILURE;
                            if ((new String(foo)).equals("exit-status")) {
                                i = buf.getInt();
                                channel.setExitStatus(i);
                                reply_type = (byte) SSH_MSG_CHANNEL_SUCCESS;
                            }
                            if (reply) {
                                packet.reset();
                                buf.putByte(reply_type);
                                buf.putInt(channel.getRecipient());
                                write(packet);
                            }
                        } else {
                        }
                        break;
                    case SSH_MSG_CHANNEL_OPEN:
                        buf.getInt();
                        buf.getShort();
                        foo = buf.getString();
                        String ctyp = new String(foo);
                        if (!"forwarded-tcpip".equals(ctyp) && !("x11".equals(ctyp) && x11_forwarding) && !("auth-agent@openssh.com".equals(ctyp) && agent_forwarding)) {
                            packet.reset();
                            buf.putByte((byte) SSH_MSG_CHANNEL_OPEN_FAILURE);
                            buf.putInt(buf.getInt());
                            buf.putInt(Channel.SSH_OPEN_ADMINISTRATIVELY_PROHIBITED);
                            buf.putString("".getBytes());
                            buf.putString("".getBytes());
                            write(packet);
                        } else {
                            channel = Channel.getChannel(ctyp);
                            addChannel(channel);
                            channel.getData(buf);
                            channel.init();
                            Thread tmp = new Thread(channel);
                            tmp.setName("Channel " + ctyp + " " + host);
                            if (daemon_thread) {
                                tmp.setDaemon(daemon_thread);
                            }
                            tmp.start();
                            break;
                        }
                    case SSH_MSG_CHANNEL_SUCCESS:
                        buf.getInt();
                        buf.getShort();
                        i = buf.getInt();
                        channel = Channel.getChannel(i, this);
                        if (channel == null) {
                            break;
                        }
                        channel.reply = 1;
                        break;
                    case SSH_MSG_CHANNEL_FAILURE:
                        buf.getInt();
                        buf.getShort();
                        i = buf.getInt();
                        channel = Channel.getChannel(i, this);
                        if (channel == null) {
                            break;
                        }
                        channel.reply = 0;
                        break;
                    case SSH_MSG_GLOBAL_REQUEST:
                        buf.getInt();
                        buf.getShort();
                        foo = buf.getString();
                        reply = (buf.getByte() != 0);
                        if (reply) {
                            packet.reset();
                            buf.putByte((byte) SSH_MSG_REQUEST_FAILURE);
                            write(packet);
                        }
                        break;
                    case SSH_MSG_REQUEST_FAILURE:
                    case SSH_MSG_REQUEST_SUCCESS:
                        Thread t = grr.getThread();
                        if (t != null) {
                            grr.setReply(msgType == SSH_MSG_REQUEST_SUCCESS ? 1 : 0);
                            t.interrupt();
                        }
                        break;
                    default:
                        throw new IOException("Unknown SSH message type " + msgType);
                }
            }
        } catch (Exception e) {
            if (JSch.getLogger().isEnabled(Logger.INFO)) {
                JSch.getLogger().log(Logger.INFO, "Caught an exception, leaving main loop due to " + e.getMessage());
            }
        }
        try {
            disconnect();
        } catch (NullPointerException e) {
        } catch (Exception e) {
        }
        isConnected = false;
    }
