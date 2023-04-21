    public void receiveMSG(MessageMSG message) {
        LOG.info("--> recieveMSG()");
        String readInData = null;
        try {
            Request response = null;
            int type = ProtocolConstants.NO_TYPE;
            if (!service.isStopped()) {
                InputDataStream input = message.getDataStream();
                byte[] rawData = DataStreamHelper.read(input);
                readInData = (new String(rawData));
                LOG.debug("received " + rawData.length + " bytes. [" + readInData + "]");
                deserializer.deserialize(rawData, handler);
                response = handler.getResult();
                type = response.getType();
                readInData = null;
            }
            try {
                if (type != ProtocolConstants.KICKED && type != ProtocolConstants.SESSION_TERMINATED) {
                    OutputDataStream os = new OutputDataStream();
                    os.setComplete();
                    message.sendRPY(os);
                }
            } catch (Exception e) {
                LOG.error("could not send confirmation [" + e.getMessage() + "]");
            }
            if (type == ProtocolConstants.JOIN_DOCUMENT) {
                PortableDocumentExt doc = (PortableDocumentExt) response.getPayload();
                addNewUsers(doc.getUsers());
                publisherId = doc.getPublisherId();
                docId = doc.getDocumentId();
                int participantId = doc.getParticipantId();
                RemoteUserSession session = SessionManager.getInstance().getSession(publisherId);
                if (session != null) {
                    SessionConnectionImpl connection = null;
                    connection = session.addSessionConnection(docId, message.getChannel());
                    connection.setParticipantId(participantId);
                    RemoteDocumentProxyExt proxy = session.getUser().getSharedDocument(docId);
                    sessionCallback = proxy.joinAccepted(connection);
                    sessionCallback.setParticipantId(participantId);
                    sessionCallback.setDocument(doc);
                } else {
                    LOG.warn("could not find RemoteUserSession for [" + publisherId + "]");
                }
            } else if (type == ProtocolConstants.KICKED) {
                String docId = ((DocumentInfo) response.getPayload()).getDocId();
                LOG.debug("user kicked for doc [" + docId + "]");
                sessionCallback.kicked();
                executeCleanup();
            } else if (type == ProtocolConstants.REQUEST) {
                ch.iserver.ace.algorithm.Request algoRequest = (ch.iserver.ace.algorithm.Request) response.getPayload();
                LOG.debug("receiveRequest(" + algoRequest + ")");
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
                String participantId = response.getUserId();
                LOG.debug("participantJoined(" + participantId + ", " + proxy.getUserDetails().getUsername() + ")");
                sessionCallback.participantJoined(Integer.parseInt(participantId), proxy);
            } else if (type == ProtocolConstants.PARTICIPANT_LEFT) {
                String reason = (String) response.getPayload();
                String participantId = response.getUserId();
                LOG.debug("participantLeft(" + participantId + ", " + reason + ")");
                sessionCallback.participantLeft(Integer.parseInt(participantId), Integer.parseInt(reason));
            } else if (type == ProtocolConstants.SESSION_TERMINATED) {
                LOG.debug("sessionTerminated()");
                sessionCallback.sessionTerminated();
                executeCleanup();
            }
        } catch (Exception e) {
            e.printStackTrace();
            LOG.error("could not process request [" + e + "]");
            NetworkServiceImpl.getInstance().getCallback().serviceFailure(FailureCodes.SESSION_FAILURE, "'" + readInData + "'", e);
        }
        LOG.debug("<-- receiveMSG");
    }
