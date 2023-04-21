    public void receiveMSG(MessageMSG message) {
        LOG.info("--> recieveMSG(" + message + ")");
        String readInData = null;
        try {
            Request response = null;
            int type = ProtocolConstants.NO_TYPE;
            if (!service.isStopped()) {
                InputDataStream input = message.getDataStream();
                byte[] rawData = DataStreamHelper.read(input);
                readInData = (new String(rawData, NetworkProperties.get(NetworkProperties.KEY_DEFAULT_ENCODING)));
                LOG.debug("received " + rawData.length + " bytes.");
                deserializer.deserialize(rawData, handler);
                response = handler.getResult();
                type = response.getType();
                readInData = null;
            }
            try {
                OutputDataStream os = new OutputDataStream();
                os.setComplete();
                message.sendRPY(os);
            } catch (Exception e) {
                LOG.error("could not send confirmation [" + e.getMessage() + "]");
            }
            if (DiscoveryManagerFactory.getDiscoveryManager().isUserAlive(response.getUserId())) {
                LOG.warn("user [" + response.getUserId() + " not alive, abort");
                cleanup();
                return;
            }
            if (sessionCallback == null && type != ProtocolConstants.JOIN_DOCUMENT) {
                try {
                    publisherId = info.getUserId();
                    docId = info.getDocId();
                    RemoteUserSession session = SessionManager.getInstance().getSession(publisherId);
                    sessionCallback = session.getUser().getSharedDocument(docId).getSessionConnectionCallback();
                    ParameterValidator.notNull("sessionConnectionCallback", sessionCallback);
                } catch (Exception e) {
                    LOG.error("error in initializing the sessionCallback [" + e + ", " + e.getMessage() + "]");
                }
            }
            if (type == ProtocolConstants.JOIN_DOCUMENT) {
                PortableDocumentExt doc = (PortableDocumentExt) response.getPayload();
                addNewUsers(doc.getUsers());
                publisherId = doc.getPublisherId();
                docId = doc.getDocumentId();
                int participantId = doc.getParticipantId();
                RemoteUserSession session = SessionManager.getInstance().getSession(publisherId);
                if (session != null) {
                    SessionConnectionImpl connection = session.addSessionConnection(docId, message.getChannel());
                    connection.setParticipantId(participantId);
                    RemoteDocumentProxyExt proxy = session.getUser().getSharedDocument(docId);
                    sessionCallback = proxy.joinAccepted(connection, doc.getParticipantIdUserMapping());
                    sessionCallback.setParticipantId(participantId);
                    sessionCallback.setDocument(doc);
                } else {
                    String msg = "could not find RemoteUserSession for [" + publisherId + "]";
                    LOG.warn(msg);
                    throw new IllegalStateException(msg);
                }
            } else if (type == ProtocolConstants.KICKED) {
                String docId = ((DocumentInfo) response.getPayload()).getDocId();
                LOG.debug("user kicked for doc [" + docId + "]");
                sessionCallback.kicked();
                executeCleanup();
            } else if (type == ProtocolConstants.REQUEST) {
                ch.iserver.ace.algorithm.Request algoRequest = (ch.iserver.ace.algorithm.Request) response.getPayload();
                LOG.debug("receiveRequest(siteid=" + algoRequest.getSiteId() + ", " + algoRequest.getTimestamp() + ", " + algoRequest.getOperation().getClass() + ")");
                String participantId = response.getUserId();
                sessionCallback.receiveRequest(Integer.parseInt(participantId), algoRequest);
            } else if (type == ProtocolConstants.CARET_UPDATE) {
                CaretUpdateMessage update = (CaretUpdateMessage) response.getPayload();
                LOG.debug("receivedCaretUpdate(" + update + ")");
                String participantId = response.getUserId();
                sessionCallback.receiveCaretUpdate(Integer.parseInt(participantId), update);
            } else if (type == ProtocolConstants.ACKNOWLEDGE) {
                Timestamp timestamp = (Timestamp) response.getPayload();
                String siteId = response.getUserId();
                LOG.debug("receiveAcknowledge(" + siteId + ", " + timestamp);
                sessionCallback.receiveAcknowledge(Integer.parseInt(siteId), timestamp);
            } else if (type == ProtocolConstants.PARTICIPANT_JOINED) {
                RemoteUserProxyExt proxy = (RemoteUserProxyExt) response.getPayload();
                addNewUser(proxy);
                String participantIdStr = response.getUserId();
                int participantId = Integer.parseInt(participantIdStr);
                RemoteUserSession session = SessionManager.getInstance().getSession(publisherId);
                RemoteDocumentProxyExt doc = session.getUser().getSharedDocument(docId);
                doc.addSessionParticipant(participantId, proxy);
                LOG.debug("participantJoined(" + participantId + ", " + proxy.getUserDetails().getUsername() + ")");
                sessionCallback.participantJoined(participantId, proxy);
            } else if (type == ProtocolConstants.PARTICIPANT_LEFT) {
                String reason = (String) response.getPayload();
                String participantIdStr = response.getUserId();
                int participantId = Integer.parseInt(participantIdStr);
                LOG.debug("participantLeft(" + participantId + ", " + reason + ")");
                RemoteUserSession session = SessionManager.getInstance().getSession(publisherId);
                RemoteDocumentProxyExt doc = session.getUser().getSharedDocument(docId);
                RemoteUserProxyExt proxy = doc.getSessionParticipant(participantId);
                if (proxy != null && DiscoveryManagerFactory.getDiscoveryManager().isUserAlive(proxy.getId())) {
                    LOG.debug("sessionCallback.participantLeft(..)");
                    sessionCallback.participantLeft(participantId, Integer.parseInt(reason));
                } else {
                    LOG.debug("participant [" + participantId + "] not alive");
                }
                doc.removeSessionParticipant(participantId);
            } else if (type == ProtocolConstants.SESSION_TERMINATED) {
                LOG.debug("sessionTerminated()");
                sessionCallback.sessionTerminated();
                executeCleanup();
            }
        } catch (Exception e) {
            String stackTrace = StackTrace.get(e);
            LOG.error("could not process request [" + stackTrace + "]");
            String name = DiscoveryManagerFactory.getDiscoveryManager().getUser(publisherId).getUserDetails().getUsername();
            name += (readInData != null) ? " [" + readInData + "]" : "";
            NetworkServiceImpl.getInstance().getCallback().serviceFailure(FailureCodes.SESSION_FAILURE, "'" + name + "'", e);
            executeCleanup();
        }
        LOG.debug("<-- receiveMSG");
    }
