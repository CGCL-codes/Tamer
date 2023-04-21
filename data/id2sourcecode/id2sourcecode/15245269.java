    @Primitive
    public static Value unix_getsockopt(final CodeRunner ctxt, final Value type, final Value socket, final Value option) throws Fail.Exception {
        final String name = Sockopt.GET_FUNC_NAMES[type.asLong()];
        final Channel ch = ctxt.getContext().getChannel(socket.asLong());
        if (ch == null) {
            Unix.fail(ctxt, name, Unix.INVALID_DESCRIPTOR_MSG);
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
                                return ds.getBroadcast() ? Value.TRUE : Value.FALSE;
                            } else {
                                Unix.fail(ctxt, "getsockopt", Unix.UNSUPPORTED_SOCKOPT_MSG);
                                return Value.UNIT;
                            }
                        case Sockopt.SO_REUSEADDR:
                            if (s != null) {
                                return s.getReuseAddress() ? Value.TRUE : Value.FALSE;
                            }
                            if (ds != null) {
                                return ds.getReuseAddress() ? Value.TRUE : Value.FALSE;
                            }
                            if (srv != null) {
                                return srv.getReuseAddress() ? Value.TRUE : Value.FALSE;
                            }
                            return Value.UNIT;
                        case Sockopt.SO_KEEPALIVE:
                            if (s != null) {
                                return s.getKeepAlive() ? Value.TRUE : Value.FALSE;
                            } else {
                                Unix.fail(ctxt, "getsockopt", Unix.UNSUPPORTED_SOCKOPT_MSG);
                                return Value.UNIT;
                            }
                        case Sockopt.SO_OOBINLINE:
                            if (s != null) {
                                return s.getOOBInline() ? Value.TRUE : Value.FALSE;
                            } else {
                                Unix.fail(ctxt, "getsockopt", Unix.UNSUPPORTED_SOCKOPT_MSG);
                                return Value.UNIT;
                            }
                        case Sockopt.TCP_NODELAY:
                            if (s != null) {
                                return s.getTcpNoDelay() ? Value.TRUE : Value.FALSE;
                            } else {
                                Unix.fail(ctxt, "getsockopt", Unix.UNSUPPORTED_SOCKOPT_MSG);
                                return Value.UNIT;
                            }
                        case Sockopt.SO_ACCEPTCONN:
                        case Sockopt.SO_DEBUG:
                        case Sockopt.SO_DONTROUTE:
                        case Sockopt.IPV6_V6ONLY:
                            Unix.fail(ctxt, "getsockopt", Unix.UNSUPPORTED_SOCKOPT_MSG);
                            return Value.UNIT;
                        default:
                            assert false : "invalid socket option";
                            return Value.UNIT;
                    }
                case Sockopt.OPTION_TYPE_INT:
                    switch(option.asLong()) {
                        case Sockopt.SO_SNDBUF:
                            if (s != null) {
                                return Value.createFromLong(s.getSendBufferSize());
                            } else if (ds != null) {
                                return Value.createFromLong(ds.getSendBufferSize());
                            } else {
                                Unix.fail(ctxt, "getsockopt_int", Unix.UNSUPPORTED_SOCKOPT_MSG);
                                return Value.UNIT;
                            }
                        case Sockopt.SO_RCVBUF:
                            if (s != null) {
                                return Value.createFromLong(s.getReceiveBufferSize());
                            } else if (srv != null) {
                                return Value.createFromLong(srv.getReceiveBufferSize());
                            } else {
                                return Value.createFromLong(ds.getReceiveBufferSize());
                            }
                        case Sockopt.SO_ERROR:
                        case Sockopt.SO_TYPE:
                        case Sockopt.SO_RCVLOWAT:
                        case Sockopt.SO_SNDLOWAT:
                            Unix.fail(ctxt, "getsockopt_int", Unix.UNSUPPORTED_SOCKOPT_MSG);
                            return Value.UNIT;
                        default:
                            assert false : "invalid socket option";
                            return Value.UNIT;
                    }
                case Sockopt.OPTION_TYPE_LINGER:
                    if (option != Value.ZERO) {
                        Unix.fail(ctxt, "getsockopt_optint", Unix.UNSUPPORTED_SOCKOPT_MSG);
                        return Value.UNIT;
                    }
                    if (s != null) {
                        final int val = s.getSoLinger();
                        if (val == -1) {
                            return Value.ZERO;
                        } else {
                            final Block res = Block.createBlock(0, Value.createFromLong(val));
                            return Value.createFromBlock(res);
                        }
                    } else {
                        Unix.fail(ctxt, "getsockopt_optint", Unix.UNSUPPORTED_SOCKOPT_MSG);
                        return Value.UNIT;
                    }
                case Sockopt.OPTION_TYPE_TIMEVAL:
                    Unix.fail(ctxt, "getsockopt_float", Unix.UNSUPPORTED_SOCKOPT_MSG);
                    return Value.UNIT;
                case Sockopt.OPTION_TYPE_UNIX_ERROR:
                    Unix.fail(ctxt, "getsockopt_error", Unix.UNSUPPORTED_SOCKOPT_MSG);
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
