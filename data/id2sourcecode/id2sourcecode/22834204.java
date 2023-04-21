    public void setupOutboundChat(String called, String transferId, String transferFrom, String transcript, long joinSessionId, ChatPanel transferView, boolean userTransfer) {
        SetupRequestMessage message = new SetupRequestMessage();
        CallPartyElement cp = (CallPartyElement) SessionInfo.getInstance().get(SessionInfo.USER_INFO);
        CallingNameElement calling = new CallingNameElement();
        calling.setCallParty(cp);
        message.setCallingNameElement(calling);
        CalledNameElement cledElement = new CalledNameElement();
        CallPartyElement cpElement = new CallPartyElement();
        cpElement.setName(called);
        cledElement.setCallParty(cpElement);
        message.setCalledNameElement(cledElement);
        this.joinSessionId = joinSessionId;
        if (transferView != null) {
            view = transferView;
        }
        if (otherParties.size() > 0) {
            otherParties.set(0, cpElement);
        } else {
            otherParties.add(cpElement);
        }
        if (xferredFromId >= 0L) {
            if (transferId != null) {
                message.setTransferId(transferId);
            }
            if (transferFrom != null) {
                message.setTransferFrom(transferFrom);
            }
            message.setUserTransfer(userTransfer);
            if (transcript != null) {
                MediaElements elements = new MediaElements();
                message.setMedia(elements);
                HtmlElement elem = new HtmlElement();
                elements.addMediaElement(elem);
                elem.setHtml("<hr><blockquote>" + transcript + "</blockquote><hr>");
            }
        } else if (joinSessionId >= 0L) {
            view.appendToConveration(systemUser, timestamp(), ApplicationController.getMessages().ChatSessionPresenter_addingUserToConference(called));
            message.setUserConference(true);
        } else {
            if (!ApplicationController.getInstance().isOperator()) {
                systemUser = called;
            }
            MessageBoxPresenter.getInstance().show(ApplicationController.getMessages().ChatSessionPresenter_chatSetup(), ApplicationController.getMessages().ChatSessionPresenter_settingUpChatWithParty(called) + " ... ", (String) Images.USER_CHATTING_MEDIUM, false);
        }
        CommunicationsFactory.getServerCommunications().sendRequest(message, Message.CONTENT_TYPE_XML, true, 100000L, new ResponseListener() {

            @Override
            public void timeoutOccured(int requestId) {
                MessageBoxPresenter.getInstance().hide();
                requestId = -1;
                abortOutboundChat(ApplicationController.getMessages().ChatSessionPresenter_noResponseFromServer());
            }

            @Override
            public void responseReceived(int requestId, String contentType, ResponseMessage message) {
                MessageBoxPresenter.getInstance().hide();
                setupRequestId = requestId;
                SetupResponseMessage rsp = (SetupResponseMessage) message.getMessage();
                if (rsp.getCalledParty() != null) {
                    otherParties.set(0, rsp.getCalledParty().getCallParty());
                }
                switch(message.getStatus()) {
                    case SetupResponseMessage.ACK:
                        handleReceivedAck(rsp);
                        break;
                    case SetupResponseMessage.ALERTING:
                        handleReceivedAlerting(rsp);
                        break;
                    case SetupResponseMessage.PROG:
                        handleReceivedProgress(requestId, rsp);
                        break;
                    case SetupResponseMessage.CONNECT:
                        handleReceivedConnect(requestId, contentType, rsp);
                        break;
                    case SetupResponseMessage.BUSY:
                        handleNotConnected(requestId, ApplicationController.getMessages().ChatSessionPresenter_busyResponse());
                        break;
                    case SetupResponseMessage.NOANS:
                        handleNotConnected(requestId, ApplicationController.getMessages().ChatSessionPresenter_noAnswerResponse());
                        break;
                    case SetupResponseMessage.UNAVAILABLE:
                        handleNotConnected(requestId, ApplicationController.getMessages().ChatSessionPresenter_notAvailableResponse());
                        break;
                    case SetupResponseMessage.UNKNOWN:
                        handleNotConnected(requestId, ApplicationController.getMessages().ChatSessionPresenter_notOnlineResponse());
                        break;
                    default:
                        CommunicationsFactory.getServerCommunications().cancelRequest(requestId);
                        abortOutboundChat(ApplicationController.getMessages().ChatSessionPresenter_chatSetupFailed() + ": " + message.getReason());
                        break;
                }
            }
        });
    }
