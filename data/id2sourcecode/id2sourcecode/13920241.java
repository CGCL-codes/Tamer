            public Boolean run(Integer xact_id, Integer code, XdrAble resp, Thunk1<Long> done_cb) {
                ob.add(xact_id.intValue());
                ob.add(1);
                ob.add(0);
                ob.add(0);
                ob.add(0);
                ob.add(code.intValue());
                XdrOutputBufferEncodingStream es = new XdrOutputBufferEncodingStream(ob);
                try {
                    resp.xdrEncode(es);
                } catch (Exception e) {
                    error_close_connection("error serializing rpc reply=" + resp + " error=" + e + " write_buf=" + ob);
                    return new Boolean(false);
                }
                LinkedList<ByteBuffer> list = ob.getList();
                int size = ob.size();
                ob.flip();
                if (write_bufs == null || write_bufs.length < list.size()) write_bufs = new ByteBuffer[list.size()];
                list.toArray(write_bufs);
                write_bufs_length = list.size();
                write_bufs_position = 0;
                write_bufs_limit = 0;
                write_xact_id = xact_id.intValue();
                ob.reset();
                for (int i = 0; i < write_bufs_length; i++) {
                    ByteBuffer packet = write_bufs[i];
                    int lastFrag = (i == write_bufs_length - 1 ? 0x80000000 : 0x00000000);
                    packet.putInt(lastFrag | (packet.limit() - 4));
                    packet.position(0);
                    write_bufs_limit += packet.limit();
                    if (logger.isDebugEnabled() && packet.hasArray()) {
                        _md.update(packet.array(), packet.arrayOffset(), packet.limit());
                        byte[] digest = _md.digest();
                        logger.debug("normal_resp to " + client_string + " for xact_id 0x" + Integer.toHexString(xact_id) + ": frag " + i + " hash: 0x" + ByteUtils.print_bytes(digest, 0, 4));
                    }
                }
                if (logger.isDebugEnabled()) logger.debug("For xact_id 0x" + Integer.toHexString(xact_id) + " to " + client_string + ", total serialized size " + size + ", total write bufs size=" + write_bufs_limit + ", in " + write_bufs_length + " packets.");
                long n = 0;
                try {
                    n = sc.write(write_bufs, 0, write_bufs_length);
                } catch (IOException e) {
                    conn_closed();
                    return new Boolean(false);
                }
                write_bufs_position += n;
                if (logger.isDebugEnabled()) logger.debug("write_cb: sent " + n + " bytes; " + (write_bufs_limit - write_bufs_position) + " bytes remaining to " + client_string + " xact_id 0x" + Integer.toHexString(write_xact_id));
                if (n == write_bufs_limit) {
                    done_cb.run(write_bufs_limit);
                    return new Boolean(true);
                } else {
                    if (logger.isDebugEnabled()) logger.debug("first write of " + n + " bytes insufficient to " + client_string + " xact_id 0x" + Integer.toHexString(write_xact_id) + ". " + " need to write " + (write_bufs_limit - write_bufs_position) + " bytes more of a total of " + write_bufs_limit + " bytes.");
                    to_write.removeFirst();
                    Function0<Boolean> func = curry(continue_write_cb, done_cb);
                    to_write.addFirst(func);
                    return new Boolean(false);
                }
            }
