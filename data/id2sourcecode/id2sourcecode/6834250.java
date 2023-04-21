    public Buffer read(Buffer buf) throws Exception {
        int j = 0;
        while (true) {
            buf.reset();
            io.getByte(buf.buffer, buf.index, s2ccipher_size);
            buf.index += s2ccipher_size;
            if (s2ccipher != null) {
                s2ccipher.update(buf.buffer, 0, s2ccipher_size, buf.buffer, 0);
            }
            j = ((buf.buffer[0] << 24) & 0xff000000) | ((buf.buffer[1] << 16) & 0x00ff0000) | ((buf.buffer[2] << 8) & 0x0000ff00) | ((buf.buffer[3]) & 0x000000ff);
            if (j < 5 || j > (32768 - 4)) {
                throw new IOException("invalid data");
            }
            j = j + 4 - s2ccipher_size;
            if ((buf.index + j) > buf.buffer.length) {
                byte[] foo = new byte[buf.index + j];
                System.arraycopy(buf.buffer, 0, foo, 0, buf.index);
                buf.buffer = foo;
            }
            if ((j % s2ccipher_size) != 0) {
                String message = "Bad packet length " + j;
                if (JSch.getLogger().isEnabled(Logger.FATAL)) {
                    JSch.getLogger().log(Logger.FATAL, message);
                }
                packet.reset();
                buf.putByte((byte) SSH_MSG_DISCONNECT);
                buf.putInt(3);
                buf.putString(message.getBytes());
                buf.putString("en".getBytes());
                write(packet);
                disconnect();
                throw new JSchException("SSH_MSG_DISCONNECT: " + message);
            }
            if (j > 0) {
                io.getByte(buf.buffer, buf.index, j);
                buf.index += (j);
                if (s2ccipher != null) {
                    s2ccipher.update(buf.buffer, s2ccipher_size, j, buf.buffer, s2ccipher_size);
                }
            }
            if (s2cmac != null) {
                s2cmac.update(seqi);
                s2cmac.update(buf.buffer, 0, buf.index);
                s2cmac.doFinal(s2cmac_result1, 0);
                io.getByte(s2cmac_result2, 0, s2cmac_result2.length);
                if (!java.util.Arrays.equals(s2cmac_result1, s2cmac_result2)) {
                    throw new IOException("MAC Error");
                }
            }
            seqi++;
            if (inflater != null) {
                int pad = buf.buffer[4];
                uncompress_len[0] = buf.index - 5 - pad;
                byte[] foo = inflater.uncompress(buf.buffer, 5, uncompress_len);
                if (foo != null) {
                    buf.buffer = foo;
                    buf.index = 5 + uncompress_len[0];
                } else {
                    System.err.println("fail in inflater");
                    break;
                }
            }
            int type = buf.getCommand() & 0xff;
            if (type == SSH_MSG_DISCONNECT) {
                buf.rewind();
                buf.getInt();
                buf.getShort();
                int reason_code = buf.getInt();
                byte[] description = buf.getString();
                byte[] language_tag = buf.getString();
                throw new JSchException("SSH_MSG_DISCONNECT: " + reason_code + " " + new String(description) + " " + new String(language_tag));
            } else if (type == SSH_MSG_IGNORE) {
            } else if (type == SSH_MSG_UNIMPLEMENTED) {
                buf.rewind();
                buf.getInt();
                buf.getShort();
                int reason_id = buf.getInt();
                if (JSch.getLogger().isEnabled(Logger.INFO)) {
                    JSch.getLogger().log(Logger.INFO, "Received SSH_MSG_UNIMPLEMENTED for " + reason_id);
                }
            } else if (type == SSH_MSG_DEBUG) {
                buf.rewind();
                buf.getInt();
                buf.getShort();
            } else if (type == SSH_MSG_CHANNEL_WINDOW_ADJUST) {
                buf.rewind();
                buf.getInt();
                buf.getShort();
                Channel c = Channel.getChannel(buf.getInt(), this);
                if (c == null) {
                } else {
                    c.addRemoteWindowSize(buf.getInt());
                }
            } else if (type == 52) {
                isAuthed = true;
                if (inflater == null && deflater == null) {
                    String method;
                    method = guess[KeyExchange.PROPOSAL_COMP_ALGS_CTOS];
                    initDeflater(method);
                    method = guess[KeyExchange.PROPOSAL_COMP_ALGS_STOC];
                    initInflater(method);
                }
                break;
            } else {
                break;
            }
        }
        buf.rewind();
        return buf;
    }
