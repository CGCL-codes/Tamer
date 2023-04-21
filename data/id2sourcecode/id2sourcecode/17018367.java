            public void run() {
                ClientSessionImpl sessionImpl = ClientSessionImpl.getSession(dataService, sessionRefId);
                if (sessionImpl != null) {
                    if (isConnected()) {
                        sessionService.getChannelService().handleChannelMessage(channelId, sessionImpl.getWrappedClientSession(), message.asReadOnlyBuffer());
                    }
                } else {
                    scheduleHandleDisconnect(false, true);
                }
            }
