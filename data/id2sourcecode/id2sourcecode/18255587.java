    @Override
    public void enter(Object param, TransmissionChannel transmissionChannel) throws ProtocolException {
        PhysicalLocality readPhysicalLocality = null;
        String error = "";
        try {
            UnMarshaler unMarshaler = getUnMarshaler(transmissionChannel);
            String next_token = null;
            if (param != null) next_token = param.toString(); else next_token = unMarshaler.readStringLine();
            if (logoutUnsubscribe) {
                if (!next_token.equals(LOGOUT_S) && !next_token.equals(UNSUBSCRIBE_S)) {
                    error = WrongStringProtocolException.format(LOGOUT_S + " or " + UNSUBSCRIBE_S, next_token);
                }
            } else {
                if (!next_token.equals(LOGIN_S) && !next_token.equals(SUBSCRIBE_S)) {
                    error = WrongStringProtocolException.format(LOGIN_S + " or " + SUBSCRIBE_S, next_token);
                }
            }
            if (error.length() > 0) {
                Marshaler marshaler = getMarshaler(transmissionChannel);
                marshaler.writeStringLine(error);
                releaseMarshaler(marshaler);
                setNextState(Protocol.END);
                return;
            }
            if ((next_token.equals(LOGIN_S) || next_token.equals(LOGOUT_S)) && logicalLocality != null) {
                Marshaler marshaler = getMarshaler(transmissionChannel);
                marshaler.writeStringLine("use SUBSCRIBE protocol, not LOGIN protocol");
                releaseMarshaler(marshaler);
                setNextState(Protocol.END);
                return;
            }
            readPhysicalLocality = new PhysicalLocality(getProtocolStack().getSession().getRemoteEnd());
            synchronized (routingTable) {
                LogicalLocality log_loc = null;
                if (logoutUnsubscribe) {
                    success = routingTable.hasRoute(readPhysicalLocality.getSessionId());
                    if (!success) System.err.println("Physical Locality " + readPhysicalLocality + " not present!");
                } else {
                    success = !routingTable.hasRoute(readPhysicalLocality.getSessionId());
                    if (!success) {
                        error = "Physical Locality " + readPhysicalLocality + " already present!";
                        System.err.println(error);
                    }
                }
                if (logicalLocality != null) {
                    log_loc = new LogicalLocality(unMarshaler.readStringLine());
                    if (logoutUnsubscribe) {
                        success = (environment.remove(log_loc) != null);
                        if (!success) System.err.println("Logical Locality " + log_loc + " not present!");
                    } else {
                        success = environment.try_add(log_loc, readPhysicalLocality);
                        if (!success) {
                            error = "Logical Locality " + log_loc + " already present!";
                            System.err.println(error);
                        }
                    }
                }
                if (success) {
                    if (logoutUnsubscribe) {
                        routingTable.removeRoute(readPhysicalLocality.getSessionId());
                    } else {
                        routingTable.addRoute(readPhysicalLocality.getSessionId(), getProtocolStack());
                    }
                    LoginSubscribeEvent loginSubscribeEvent = null;
                    if (logicalLocality == null) {
                        loginSubscribeEvent = new LoginSubscribeEvent(this, getSession());
                        loginSubscribeEvent.removed = logoutUnsubscribe;
                        generate((logoutUnsubscribe ? LoginSubscribeEvent.LOGOUT_EVENT : LoginSubscribeEvent.LOGIN_EVENT), loginSubscribeEvent);
                    } else {
                        loginSubscribeEvent = new LoginSubscribeEvent(this, getSession(), log_loc);
                        loginSubscribeEvent.removed = logoutUnsubscribe;
                        generate((logoutUnsubscribe ? LoginSubscribeEvent.UNSUBSCRIBE_EVENT : LoginSubscribeEvent.SUBSCRIBE_EVENT), loginSubscribeEvent);
                    }
                    physicalLocality.setValue(readPhysicalLocality);
                    if (logicalLocality != null) logicalLocality.setValue(log_loc);
                }
            }
            if (!logoutUnsubscribe) {
                Marshaler marshaler = getMarshaler(transmissionChannel);
                if (success) {
                    marshaler.writeStringLine("OK");
                    marshaler.writeStringLine("" + readPhysicalLocality);
                } else {
                    marshaler.writeStringLine("FAIL");
                    marshaler.writeStringLine(error);
                }
                releaseMarshaler(marshaler);
            }
            if (!logoutUnsubscribe && success) {
                Protocol protocol = messageProtocolFactory.createProtocol();
                protocolStack.insertFirstLayer(new MessageProtocolLayer());
                protocol.setProtocolStack(protocolStack);
                ProtocolThread protocolThread = new ProtocolThread(protocol);
                protocolThread.start();
            }
        } catch (IOException e) {
            throw new ProtocolException(e);
        }
        if (!success) setNextState(Protocol.END);
    }
