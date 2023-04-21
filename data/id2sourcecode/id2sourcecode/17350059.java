    public void run() {
        Mid mid = new Mid();
        int i, m, nbtlen;
        ServerMessageBlock response;
        while (thread == Thread.currentThread()) {
            try {
                socket.setSoTimeout(SO_TIMEOUT);
                m = 0;
                while (m < 4) {
                    if ((i = in.read()) < 0) {
                        return;
                    }
                    if ((i & 0xFF) == MAGIC[m]) {
                        m++;
                    } else if ((i & 0xFF) == 0xFF) {
                        m = 1;
                    } else {
                        m = 0;
                    }
                }
                nbtlen = 4 + in.available();
                synchronized (rcv_buf) {
                    rcv_buf[0] = (byte) 0xFF;
                    rcv_buf[1] = (byte) 'S';
                    rcv_buf[2] = (byte) 'M';
                    rcv_buf[3] = (byte) 'B';
                    if (in.read(rcv_buf, 4, ServerMessageBlock.HEADER_LENGTH - 4) != (ServerMessageBlock.HEADER_LENGTH - 4)) {
                        break;
                    }
                    ((PushbackInputStream) in).unread(rcv_buf, 0, ServerMessageBlock.HEADER_LENGTH);
                    if (rcv_buf[0] != (byte) 0xFF || rcv_buf[1] != (byte) 'S' || rcv_buf[2] != (byte) 'M' || rcv_buf[3] != (byte) 'B') {
                        if (DebugFile.trace) DebugFile.writeln("bad smb header, purging session message: " + address);
                        in.skip(in.available());
                        continue;
                    }
                    if ((rcv_buf[ServerMessageBlock.FLAGS_OFFSET] & ServerMessageBlock.FLAGS_RESPONSE) == ServerMessageBlock.FLAGS_RESPONSE) {
                        mid.mid = (short) (ServerMessageBlock.readInt2(rcv_buf, MID_OFFSET) & 0xFFFF);
                        response = (ServerMessageBlock) responseTable.get(mid);
                        if (response == null) {
                            if (DebugFile.trace) {
                                DebugFile.writeln("no handler for mid=" + mid.mid + ", purging session message: " + address);
                            }
                            in.skip(in.available());
                            continue;
                        }
                        synchronized (response) {
                            response.useUnicode = useUnicode;
                            if (DebugFile.trace) DebugFile.writeln("new data read from socket: " + address);
                            if (response instanceof SmbComTransactionResponse) {
                                Enumeration e = (Enumeration) response;
                                if (e.hasMoreElements()) {
                                    e.nextElement();
                                } else {
                                    if (DebugFile.trace) DebugFile.writeln("more responses to transaction than expected");
                                    continue;
                                }
                                if ((m = response.readWireFormat(in, rcv_buf, 0)) != nbtlen) {
                                    if (DebugFile.trace) {
                                        DebugFile.writeln("decoded " + m + " but nbtlen=" + nbtlen + ", purging session message");
                                    }
                                    in.skip(in.available());
                                }
                                response.received = true;
                                if (response.errorCode != 0 || e.hasMoreElements() == false) {
                                    ((SmbComTransactionResponse) response).hasMore = false;
                                    if (digest != null) {
                                        synchronized (outLock) {
                                            digest.verify(rcv_buf, 0, response);
                                        }
                                    }
                                    response.notify();
                                } else {
                                    ensureOpen();
                                }
                            } else {
                                response.readWireFormat(in, rcv_buf, 0);
                                response.received = true;
                                if (digest != null) {
                                    synchronized (outLock) {
                                        digest.verify(rcv_buf, 0, response);
                                    }
                                }
                                response.notify();
                            }
                        }
                    } else {
                    }
                }
            } catch (InterruptedIOException iioe) {
                if (responseTable.size() == 0) {
                    tryClose(false);
                } else if (DebugFile.trace) {
                    DebugFile.writeln("soTimeout has occured but there are " + responseTable.size() + " pending requests");
                }
            } catch (IOException ioe) {
                synchronized (this) {
                    tryClose(true);
                }
                if (DebugFile.trace && ioe.getMessage().startsWith("Connection reset") == false) {
                    DebugFile.writeln(ioe.getMessage() + ": " + address);
                    new ErrorHandler(ioe);
                }
            }
        }
    }
