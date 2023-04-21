    private void handleDelayedEvents() {
        for (Iterator it = delayedEvents.iterator(); it.hasNext(); ) {
            EventInfo info = (EventInfo) it.next();
            switch(info.getType()) {
                case EventInfo.TYPE_JOIN:
                    userJoined(info.getUser(), info.getChannel());
                    break;
                case EventInfo.TYPE_LEFT:
                    userLeft(info.getUser(), info.getChannel());
                    break;
                case EventInfo.TYPE_MODE:
                    userModeChange(info.getChannel(), info.getUser(), info.getMode(), info.isAdding());
                    break;
                case EventInfo.TYPE_NICK:
                    userChangesNick(info.getUser(), info.getNick());
                    break;
                case EventInfo.TYPE_QUIT:
                    userQuit(info.getUser());
                    break;
            }
            it.remove();
        }
    }
