    public void handleEvent(Event event) {
        if (UserUtterance.class == event.getClass()) {
            if (((UserUtterance) event).getChannel() == CommunicationChannel.JABBER) {
                setUserIsOnline(CommunicationChannel.JABBER, true);
            }
            lastUserEvent = event;
            dispatchUserUtterance((UserUtterance) event);
            final String text = ((UserUtterance) event).getText();
            dialogueHistory.add(new DialogueAct(DialogueActType.USER_UTTERANCE, text));
        } else if (UserPraise.class == event.getClass()) {
            if (log.isDebugEnabled()) {
                log.debug("got praise event");
            }
            setUserIsOnline(((UserPraise) event).getChannel(), true);
            dispatchUserInputWithUtteranceClass(UtteranceClass.PRAISE);
            dialogueHistory.add(new DialogueAct(DialogueActType.USER_PRAISE));
        } else if (UserScolding.class == event.getClass()) {
            if (log.isDebugEnabled()) {
                log.debug("got scolding event");
            }
            setUserIsOnline(((UserScolding) event).getChannel(), true);
            dispatchUserInputWithUtteranceClass(UtteranceClass.SCOLDING);
            dialogueHistory.add(new DialogueAct(DialogueActType.USER_SCOLDING));
        } else if (UserConnected.class == event.getClass()) {
            setNebulaCommunicationProtocol(((UserConnected) event).getNebulaCommunicationProtocol());
            setUserIsOnline(CommunicationChannel.ECA, true);
            rascalliWS.setActiveRascalloIdForUser(getUser().getId(), getId());
        } else if (UserDisconnected.class == event.getClass()) {
            setUserIsOnline(CommunicationChannel.ECA, false);
            setNebulaCommunicationProtocol(null);
        } else if (JabberPresenceChanged.class == event.getClass()) {
            JabberPresenceChanged jpc = (JabberPresenceChanged) event;
            log.debug("JabberPresenceChanged: " + jpc.getJabberId() + " is " + (jpc.isOnline() ? "online" : "offline"));
            if (jpc.getJabberId().equals(user.getJabberId())) {
                final boolean isOnline = jpc.isOnline();
                setUserIsOnline(CommunicationChannel.JABBER, isOnline);
            }
        }
        for (EventListener eventListener : eventListeners) {
            eventListener.handleEvent(event);
        }
    }
