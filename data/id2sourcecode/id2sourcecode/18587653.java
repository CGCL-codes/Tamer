    private void handleInternalMessage(Message message) throws IOException {
        if (message.isRequest()) {
            String t = message.getType().getTypeName();
            if (!t.equals("com.sun.star.bridge.XProtocolProperties")) {
                throw new IOException("read URP protocol properties request with unsupported" + " type " + t);
            }
            int fid = message.getMethod().getIndex();
            switch(fid) {
                case PROPERTIES_FID_REQUEST_CHANGE:
                    checkSynchronousPropertyRequest(message);
                    synchronized (monitor) {
                        switch(state) {
                            case STATE_INITIAL0:
                            case STATE_INITIAL:
                                writeReply(false, message.getThreadId(), new Integer(1));
                                state = STATE_WAIT;
                                break;
                            case STATE_REQUESTED:
                                int n = ((Integer) message.getArguments()[0]).intValue();
                                if (random < n) {
                                    writeReply(false, message.getThreadId(), new Integer(1));
                                    state = STATE_WAIT;
                                } else if (random == n) {
                                    writeReply(false, message.getThreadId(), new Integer(-1));
                                    state = STATE_INITIAL;
                                    sendRequestChange();
                                } else {
                                    writeReply(false, message.getThreadId(), new Integer(0));
                                }
                                break;
                            default:
                                writeReply(true, message.getThreadId(), new com.sun.star.uno.RuntimeException("read URP protocol properties requestChange" + " request in illegal state"));
                                break;
                        }
                    }
                    break;
                case PROPERTIES_FID_COMMIT_CHANGE:
                    checkSynchronousPropertyRequest(message);
                    synchronized (monitor) {
                        if (state == STATE_WAIT) {
                            ProtocolProperty[] p = (ProtocolProperty[]) message.getArguments()[0];
                            boolean ok = true;
                            boolean cc = false;
                            int i = 0;
                            for (; i < p.length; ++i) {
                                if (p[i].Name.equals(PROPERTY_CURRENT_CONTEXT)) {
                                    cc = true;
                                } else {
                                    ok = false;
                                    break;
                                }
                            }
                            if (ok) {
                                writeReply(false, message.getThreadId(), null);
                            } else {
                                writeReply(true, message.getThreadId(), new InvalidProtocolChangeException("", null, p[i], 1));
                            }
                            state = STATE_INITIAL;
                            if (!initialized) {
                                if (cc) {
                                    currentContext = true;
                                    initialized = true;
                                    monitor.notifyAll();
                                } else {
                                    sendRequestChange();
                                }
                            }
                        } else {
                            writeReply(true, message.getThreadId(), new com.sun.star.uno.RuntimeException("read URP protocol properties commitChange" + " request in illegal state"));
                        }
                    }
                    break;
                default:
                    throw new IOException("read URP protocol properties request with unsupported" + " function ID " + fid);
            }
        } else {
            synchronized (monitor) {
                if (state == STATE_COMMITTED) {
                    if (!message.isAbnormalTermination()) {
                        currentContext = true;
                    }
                    state = STATE_INITIAL;
                    initialized = true;
                    monitor.notifyAll();
                } else {
                    if (message.isAbnormalTermination()) {
                        state = STATE_INITIAL;
                        initialized = true;
                        monitor.notifyAll();
                    } else {
                        int n = ((Integer) message.getResult()).intValue();
                        switch(n) {
                            case -1:
                            case 0:
                                break;
                            case 1:
                                writeRequest(true, PROPERTIES_OID, TypeDescription.getTypeDescription(XProtocolProperties.class), PROPERTIES_FUN_COMMIT_CHANGE, propertiesTid, new Object[] { new ProtocolProperty[] { new ProtocolProperty(PROPERTY_CURRENT_CONTEXT, Any.VOID) } });
                                state = STATE_COMMITTED;
                                break;
                            default:
                                throw new IOException("read URP protocol properties " + PROPERTIES_FUN_REQUEST_CHANGE + " reply with illegal return value " + n);
                        }
                    }
                }
            }
        }
    }
