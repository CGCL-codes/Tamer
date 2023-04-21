    @Primitive
    public static Value unix_setsockopt(final CodeRunner ctxt, final Value type, final Value socket, final Value option, final Value status) throws Fail.Exception {
        final String name = Sockopt.SET_FUNC_NAMES[type.asLong()];
        final Channel ch = ctxt.getContext().getChannel(socket.asLong());
        if (ch == null) {
            Unix.fail(ctxt, "setsockopt_bool", Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
        final java.net.Socket s = ch.asSocket();
        final DatagramSocket ds = ch.asDatagramSocket();
        final ServerSocket srv = ch.asServerSocket();
        if ((s == null) && (ds == null) && (srv == null)) {
            Unix.fail(ctxt, name, Unix.INVALID_DESCRIPTOR_MSG);
            return Value.UNIT;
        }
        try {
            switch(type.asLong()) {
                case Sockopt.OPTION_TYPE_BOOL:
                    switch(option.asLong()) {
                        case Sockopt.SO_BROADCAST:
                            if (ds != null) {
                                ds.setBroadcast(status == Value.TRUE);
                            } else {
                                Unix.fail(ctxt, "setsockopt_bool", Unix.UNSUPPORTED_SOCKOPT_MSG);
                            }
                            return Value.UNIT;
                        case Sockopt.SO_REUSEADDR:
                            if (s != null) {
                                s.setReuseAddress(status == Value.TRUE);
                            }
                            if (ds != null) {
                                ds.setReuseAddress(status == Value.TRUE);
                            }
                            if (srv != null) {
                                srv.setReuseAddress(status == Value.TRUE);
                            }
                            return Value.UNIT;
                        case Sockopt.SO_KEEPALIVE:
                            if (s != null) {
                                s.setKeepAlive(status == Value.TRUE);
                            } else {
                                Unix.fail(ctxt, "setsockopt_bool", Unix.UNSUPPORTED_SOCKOPT_MSG);
                            }
                            return Value.UNIT;
                        case Sockopt.SO_OOBINLINE:
                            if (s != null) {
                                s.setOOBInline(status == Value.TRUE);
                            } else {
                                Unix.fail(ctxt, "setsockopt_bool", Unix.UNSUPPORTED_SOCKOPT_MSG);
                            }
                            return Value.UNIT;
                        case Sockopt.TCP_NODELAY:
                            if (s != null) {
                                s.setTcpNoDelay(status == Value.TRUE);
                                return Value.UNIT;
                            } else {
                                Unix.fail(ctxt, "setsockopt_bool", Unix.UNSUPPORTED_SOCKOPT_MSG);
                                return Value.UNIT;
                            }
                        case Sockopt.SO_ACCEPTCONN:
                        case Sockopt.SO_DEBUG:
                        case Sockopt.SO_DONTROUTE:
                        case Sockopt.IPV6_V6ONLY:
                            Unix.fail(ctxt, "setsockopt_bool", Unix.UNSUPPORTED_SOCKOPT_MSG);
                            return Value.UNIT;
                        default:
                            assert false : "invalid socket option";
                            return Value.UNIT;
                    }
                case Sockopt.OPTION_TYPE_INT:
                    switch(option.asLong()) {
                        case Sockopt.SO_SNDBUF:
                            if (s != null) {
                                s.setSendBufferSize(status.asLong());
                            } else if (ds != null) {
                                ds.setSendBufferSize(status.asLong());
                            } else {
                                Unix.fail(ctxt, "setsockopt_int", Unix.UNSUPPORTED_SOCKOPT_MSG);
                                return Value.UNIT;
                            }
                            break;
                        case Sockopt.SO_RCVBUF:
                            if (s != null) {
                                s.setReceiveBufferSize(status.asLong());
                            } else if (srv != null) {
                                srv.setReceiveBufferSize(status.asLong());
                            } else {
                                ds.setReceiveBufferSize(status.asLong());
                            }
                            break;
                        case Sockopt.SO_ERROR:
                        case Sockopt.SO_TYPE:
                        case Sockopt.SO_RCVLOWAT:
                        case Sockopt.SO_SNDLOWAT:
                            Unix.fail(ctxt, "setsockopt_int", Unix.UNSUPPORTED_SOCKOPT_MSG);
                            break;
                        default:
                            assert false : "invalid socket option";
                    }
                    return Value.UNIT;
                case Sockopt.OPTION_TYPE_LINGER:
                    if (option != Value.ZERO) {
                        Unix.fail(ctxt, "setsockopt_optint", Unix.UNSUPPORTED_SOCKOPT_MSG);
                        return Value.UNIT;
                    }
                    if (s != null) {
                        if (status.isBlock()) {
                            s.setSoLinger(true, status.asBlock().get(0).asLong());
                        } else {
                            s.setSoLinger(false, 0);
                        }
                        return Value.UNIT;
                    } else {
                        Unix.fail(ctxt, "setsockopt_optint", Unix.INVALID_DESCRIPTOR_MSG);
                        return Value.UNIT;
                    }
                case Sockopt.OPTION_TYPE_TIMEVAL:
                    Unix.fail(ctxt, "setsockopt_float", Unix.UNSUPPORTED_SOCKOPT_MSG);
                    return Value.UNIT;
                case Sockopt.OPTION_TYPE_UNIX_ERROR:
                    Unix.fail(ctxt, "setsockopt_error", Unix.UNSUPPORTED_SOCKOPT_MSG);
                    return Value.UNIT;
                default:
                    assert false : "invalid socket option";
                    return Value.UNIT;
            }
        } catch (final SocketException se) {
            Unix.fail(ctxt, name, se);
            return Value.UNIT;
        }
    }
