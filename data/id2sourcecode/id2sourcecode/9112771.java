        public void handleEvent(ManagerEvent _event) {
            if (config.debug > 0) {
                System.out.println("* event: " + ToString.convert(_event));
            }
            if (_event instanceof OriginateSuccessEvent) {
                OriginateSuccessEvent ev = (OriginateSuccessEvent) _event;
                AsteriskCall _call = (AsteriskCall) id2call.get(ev.getUniqueId());
                if (_call != null && !_call.originate && config.outgoingContext.equals(ev.getContext())) {
                    String s = ev.getChannel();
                    if (s.startsWith("local/")) {
                        s = s.substring("local/".length());
                        int i = s.indexOf('@');
                        if (i > 0) {
                            String _terminal = s.substring(0, i);
                            String _context = s.substring(i + 1);
                            _call.originate = true;
                            _call.calledAddress = ev.getExten();
                            _call.leg2.address = ev.getExten();
                            _call.address = _call.firstCallerId;
                            _call.terminal = _terminal;
                            listener.connectionConnected(_call, _call.address, CallEvent.CAUSE_NEW_CALL);
                            listener.terminalConnectionTalking(_call, _call.address, _call.terminal, CallEvent.CAUSE_NEW_CALL);
                            listener.connectionInProgress(_call, _call.calledAddress, CallEvent.CAUSE_NEW_CALL);
                        }
                    }
                }
                return;
            }
            if (_event instanceof NewStateEvent) {
                NewStateEvent ev = (NewStateEvent) _event;
                AsteriskCall _call = (AsteriskCall) id2call.get(ev.getUniqueId());
                if (_call == null || _call.address == null) {
                    return;
                }
                if (ev.getState().equals("Ringing")) {
                    listener.connectionAlerting(_call.realCall, _call.address, CallEvent.CAUSE_NORMAL);
                    if (_call.terminal != null) {
                        listener.terminalConnectionRinging(_call.realCall, _call.address, _call.terminal, CallEvent.CAUSE_NORMAL);
                    }
                } else if (ev.getState().equals("Up")) {
                    listener.connectionConnected(_call.realCall, _call.address, CallEvent.CALL_ACTIVE);
                    if (_call.terminal != null) {
                        listener.terminalConnectionTalking(_call.realCall, _call.address, _call.terminal, CallEvent.CAUSE_NORMAL);
                    }
                }
                return;
            }
            if (_event instanceof NewCallerIdEvent) {
                NewCallerIdEvent ev = (NewCallerIdEvent) _event;
                AsteriskCall _call = getCall(ev.getUniqueId());
                if (_call.firstCallerId == null) {
                    _call.firstCallerId = ev.getCallerId();
                }
                _call.callerId = ev.getCallerId();
                return;
            }
            if (_event instanceof NewExtenEvent) {
                NewExtenEvent nee = (NewExtenEvent) _event;
                if (config.incomingContext.contains(nee.getContext())) {
                    AsteriskCall _call = getCall(nee.getUniqueId());
                    if (_call.address == null) {
                        _call.address = nee.getExtension();
                        if (_call.callerId != null) {
                            listener.connectionConnected(_call, _call.callerId, CallEvent.CAUSE_NEW_CALL);
                        }
                        listener.connectionInProgress(_call, _call.address, CallEvent.CAUSE_NEW_CALL);
                    }
                }
                if (config.terminalContext.contains(nee.getContext())) {
                    AsteriskCall _call = (AsteriskCall) id2call.get(nee.getUniqueId());
                    if (_call != null) {
                        _call.terminal = nee.getExtension();
                    }
                }
                return;
            }
            if (_event instanceof DialEvent) {
                DialEvent de = (DialEvent) _event;
                AsteriskCall _call = (AsteriskCall) id2call.get(de.getSrcUniqueId());
                if (_call != null) {
                    AsteriskCall c2 = getCall(de.getDestUniqueId());
                    c2.realCall = _call;
                    c2.address = _call.calledAddress;
                    _call.leg2 = c2;
                }
            }
            if (_event instanceof ChannelEvent) {
                ChannelEvent ce = (ChannelEvent) _event;
                AsteriskCall _call = (AsteriskCall) id2call.get(ce.getUniqueId());
                if (_call == null) {
                    return;
                }
                if (ce instanceof NewChannelEvent) {
                    String _state = ce.getState();
                    if ("Ring".equals(_state)) {
                        _call.callerId = ce.getCallerId();
                        return;
                    }
                }
                if (_event instanceof HangupEvent) {
                    if (_call.callerId != null && _call.leg2 == null) {
                        listener.connectionDisconnected(_call, _call.callerId, CallEvent.CAUSE_NORMAL);
                    }
                    if (_call.terminal != null) {
                        listener.terminalConnectionDropped(_call.realCall, _call.address, _call.terminal, CallEvent.CAUSE_NORMAL);
                    }
                    listener.connectionDisconnected(_call.realCall, _call.address, CallEvent.CAUSE_NORMAL);
                    id2call.remove(ce.getUniqueId());
                }
            }
        }
