    public void onInvoke(RTMPConnection conn, Channel channel, Header source, Notify invoke) {
        log.debug("Invoke");
        final IServiceCall call = invoke.getCall();
        if (call.getServiceMethodName().equals("_result") || call.getServiceMethodName().equals("_error")) {
            final IPendingServiceCall pendingCall = conn.getPendingCall(invoke.getInvokeId());
            if (pendingCall != null) {
                Object[] args = call.getArguments();
                if ((args != null) && (args.length > 0)) {
                    pendingCall.setResult(args[0]);
                }
                Set<IPendingServiceCallback> callbacks = pendingCall.getCallbacks();
                if (callbacks.isEmpty()) {
                    return;
                }
                HashSet<IPendingServiceCallback> tmp = new HashSet<IPendingServiceCallback>();
                tmp.addAll(callbacks);
                Iterator<IPendingServiceCallback> it = tmp.iterator();
                while (it.hasNext()) {
                    IPendingServiceCallback callback = it.next();
                    try {
                        callback.resultReceived(pendingCall);
                    } catch (Exception e) {
                        log.error("Error while executing callback " + callback, e);
                    }
                }
            }
            return;
        }
        synchronized (conn.invokeId) {
            if (conn.invokeId <= invoke.getInvokeId()) {
                conn.invokeId = invoke.getInvokeId() + 1;
            }
        }
        boolean disconnectOnReturn = false;
        if (call.getServiceName() == null) {
            log.info("call: " + call);
            final String action = call.getServiceMethodName();
            log.info("--" + action);
            if (!conn.isConnected()) {
                if (action.equals(ACTION_CONNECT)) {
                    log.debug("connect");
                    final Map params = invoke.getConnectionParams();
                    String host = getHostname((String) params.get("tcUrl"));
                    if (host.endsWith(":1935")) {
                        host = host.substring(0, host.length() - 5);
                    }
                    final String path = (String) params.get("app");
                    final String sessionId = null;
                    conn.setup(host, path, sessionId, params);
                    try {
                        final IGlobalScope global = server.lookupGlobal(host, path);
                        if (global == null) {
                            call.setStatus(Call.STATUS_SERVICE_NOT_FOUND);
                            if (call instanceof IPendingServiceCall) {
                                ((IPendingServiceCall) call).setResult(getStatus(NC_CONNECT_FAILED));
                            }
                            log.info("No global scope found for " + path + " on " + host);
                            conn.close();
                        } else {
                            final IContext context = global.getContext();
                            IScope scope = null;
                            try {
                                scope = context.resolveScope(path);
                            } catch (ScopeNotFoundException err) {
                                call.setStatus(Call.STATUS_SERVICE_NOT_FOUND);
                                if (call instanceof IPendingServiceCall) {
                                    ((IPendingServiceCall) call).setResult(getStatus(NC_CONNECT_FAILED));
                                }
                                log.info("Scope " + path + " not found on " + host);
                                disconnectOnReturn = true;
                            }
                            if (scope != null) {
                                log.info("Connecting to: " + scope);
                                boolean okayToConnect;
                                try {
                                    if (call.getArguments() != null) {
                                        okayToConnect = conn.connect(scope, call.getArguments());
                                    } else {
                                        okayToConnect = conn.connect(scope);
                                    }
                                    if (okayToConnect) {
                                        log.debug("connected");
                                        log.debug("client: " + conn.getClient());
                                        call.setStatus(Call.STATUS_SUCCESS_RESULT);
                                        if (call instanceof IPendingServiceCall) {
                                            ((IPendingServiceCall) call).setResult(getStatus(NC_CONNECT_SUCCESS));
                                        }
                                        conn.getChannel((byte) 2).write(new Ping((short) 0, 0, -1));
                                        conn.ping();
                                    } else {
                                        log.debug("connect failed");
                                        call.setStatus(Call.STATUS_ACCESS_DENIED);
                                        if (call instanceof IPendingServiceCall) {
                                            ((IPendingServiceCall) call).setResult(getStatus(NC_CONNECT_REJECTED));
                                        }
                                        disconnectOnReturn = true;
                                    }
                                } catch (ClientRejectedException rejected) {
                                    log.debug("connect rejected");
                                    call.setStatus(Call.STATUS_ACCESS_DENIED);
                                    if (call instanceof IPendingServiceCall) {
                                        StatusObject status = (StatusObject) getStatus(NC_CONNECT_REJECTED);
                                        status.setApplication(rejected.getReason());
                                        ((IPendingServiceCall) call).setResult(status);
                                    }
                                    disconnectOnReturn = true;
                                }
                            }
                        }
                    } catch (RuntimeException e) {
                        call.setStatus(Call.STATUS_GENERAL_EXCEPTION);
                        if (call instanceof IPendingServiceCall) {
                            ((IPendingServiceCall) call).setResult(getStatus(NC_CONNECT_FAILED));
                        }
                        log.error("Error connecting", e);
                        disconnectOnReturn = true;
                    }
                }
            } else if (action.equals(ACTION_DISCONNECT)) {
                conn.close();
            } else if (action.equals(ACTION_CREATE_STREAM) || action.equals(ACTION_DELETE_STREAM) || action.equals(ACTION_PUBLISH) || action.equals(ACTION_PLAY) || action.equals(ACTION_SEEK) || action.equals(ACTION_PAUSE) || action.equals(ACTION_CLOSE_STREAM) || action.equals(ACTION_RECEIVE_VIDEO) || action.equals(ACTION_RECEIVE_AUDIO)) {
                IStreamService streamService = (IStreamService) getScopeService(conn.getScope(), IStreamService.STREAM_SERVICE, StreamService.class);
                invokeCall(conn, call, streamService);
            } else {
                invokeCall(conn, call);
            }
        } else if (conn.isConnected()) {
            invokeCall(conn, call);
        } else {
            log.warn("Not connected, closing connection");
            conn.close();
        }
        if (invoke instanceof Invoke) {
            if ((source.getStreamId() != 0) && (call.getStatus() == Call.STATUS_SUCCESS_VOID || call.getStatus() == Call.STATUS_SUCCESS_NULL)) {
                log.debug("Method does not have return value, do not reply");
                return;
            }
            Invoke reply = new Invoke();
            reply.setCall(call);
            reply.setInvokeId(invoke.getInvokeId());
            log.debug("sending reply");
            channel.write(reply);
            if (disconnectOnReturn) {
                conn.close();
            }
        }
    }
