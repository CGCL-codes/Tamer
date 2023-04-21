    public Token getChannelSuccessToken(WebSocketConnector aConnector, String aChannel, ChannelEventEnum aEventType) {
        Token lToken = getBaseChannelResponse(aConnector, aChannel);
        String lEvent = "";
        switch(aEventType) {
            case LOGIN:
                lEvent = "login";
                break;
            case AUTHORIZE:
                lEvent = "authorize";
                break;
            case PUBLISH:
                lEvent = "publish";
                break;
            case SUSCRIBE:
                lEvent = "subscribe";
            case UNSUSCRIBE:
                lEvent = "unsuscribe";
                break;
            default:
                break;
        }
        lToken.setString("event", lEvent);
        lToken.setString("status", "ok");
        return lToken;
    }
