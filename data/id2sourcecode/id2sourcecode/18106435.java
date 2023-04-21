            public void run() {
                assert connection_open : "write_cb " + gnid + ": connection already closed";
                if (logger.isDebugEnabled()) logger.debug("op_write to " + gnid + (write_xact_id == -1 ? "" : ", write_xact_id 0x" + Integer.toHexString(write_xact_id)));
                while (true) {
                    if (write_bufs != null && (write_bufs_position < write_bufs_limit)) {
                        if (logger.isDebugEnabled()) {
                            String str = "";
                            for (ByteBuffer write_buf : write_bufs) {
                                if (write_buf != null) {
                                    str += "fragment:\n" + (write_buf.hasArray() ? ByteUtils.print_bytes(write_buf.array(), write_buf.arrayOffset() + write_buf.position(), write_buf.limit() - write_buf.position()) : "no backing array to show bytes");
                                }
                            }
                            if (logger.isDebugEnabled()) logger.debug("sending " + write_bufs_length + " fragments" + " to " + gnid + " , write_xact_id " + Integer.toHexString(write_xact_id) + ":\n" + str);
                        }
                        long n = 0;
                        try {
                            n = sc.write(write_bufs, 0, write_bufs_length);
                        } catch (IOException e) {
                            error_close_connection("error while writing to " + gnid + " write_xact_id 0x" + Integer.toHexString(write_xact_id) + ". " + e);
                            return;
                        }
                        write_bufs_position += n;
                        if (logger.isDebugEnabled()) logger.debug("sent " + n + " bytes; " + (write_bufs_limit - write_bufs_position) + " bytes remaining to send to " + gnid + ", write_xact_id 0x" + Integer.toHexString(write_xact_id));
                        if (write_bufs_position < write_bufs_limit) break;
                        ValueTriple<Long, Double, RpcCall> triple = inflight.get(write_xact_id);
                        RpcCall rpc_call_sent = triple.getThird();
                        if (rpc_call_sent.send_cb != null) {
                            Runnable cb = curry(rpc_call_sent.send_cb, rpc_call_sent);
                            _acoreMain.register_timer(0L, cb);
                        }
                    }
                    for (int i = 0; i < write_bufs_length; i++) {
                        if (_reuse.size() < MAX_FRAGS_CACHE) _reuse.addFirst(write_bufs[i]);
                        write_bufs[i] = null;
                    }
                    write_bufs_position = write_bufs_limit = 0L;
                    write_bufs_length = 0;
                    write_xact_id = -1;
                    if (waiting.isEmpty()) {
                        try {
                            _acore.unregister_selectable(sc, OP_WRITE);
                        } catch (ClosedChannelException e) {
                            conn_closed();
                        } catch (java.nio.channels.CancelledKeyException cke) {
                            BUG(cke);
                        }
                        break;
                    }
                    ValuePair<Long, RpcCall> pair = waiting.removeFirst();
                    Long waiting_start_time_us = pair.getFirst();
                    RpcCall req = pair.getSecond();
                    int xact_id = next_xact_id++;
                    ob.add(xact_id);
                    ob.add(0);
                    ob.add(2);
                    ob.add(req.proc.getProgNum());
                    ob.add(req.proc.getProgVer());
                    ob.add(req.proc.getProcNum());
                    for (int i = 0; i < 2; ++i) {
                        ob.add(0);
                        ob.add(0);
                    }
                    XdrOutputBufferEncodingStream es = new XdrOutputBufferEncodingStream(ob);
                    try {
                        req.args.xdrEncode(es);
                    } catch (Exception e) {
                        error_close_connection("error serializing rpc" + " write_xact_id 0x" + Integer.toHexString(write_xact_id) + " call=" + req + ". error=" + e + ", write_bufs=" + ob);
                        return;
                    }
                    LinkedList<ByteBuffer> list = ob.getList();
                    int size = ob.size();
                    ob.flip();
                    if (write_bufs == null || write_bufs.length < list.size()) write_bufs = new ByteBuffer[list.size()];
                    list.toArray(write_bufs);
                    write_bufs_length = list.size();
                    write_bufs_position = 0;
                    write_bufs_limit = 0;
                    write_xact_id = xact_id;
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
                            logger.debug("write_cb to " + gnid + " for xact_id 0x" + Integer.toHexString(xact_id) + ": frag " + i + " hash: 0x" + ByteUtils.print_bytes(digest, 0, 4));
                        }
                    }
                    if (logger.isDebugEnabled()) logger.debug("For xact_id 0x" + Integer.toHexString(xact_id) + " to " + gnid + ", total serialized size " + size + ", total write bufs size=" + write_bufs_limit + ", in " + write_bufs_length + " packets.");
                    if (inflight.size() == 0) {
                        try {
                            _acore.register_selectable(sc, OP_READ, read_cb);
                        } catch (ClosedChannelException e) {
                            conn_closed();
                        } catch (java.nio.channels.CancelledKeyException cke) {
                            BUG(cke);
                        }
                    }
                    ValueTriple<Long, Double, RpcCall> triple = new ValueTriple<Long, Double, RpcCall>(now_us(), ((now_us() - waiting_start_time_us) / 1000.0), req);
                    req.xact_id = xact_id;
                    log_req(false, req, triple.getSecond(), write_bufs_limit);
                    inflight.put(xact_id, triple);
                }
                last_activity_us = now_us();
            }
