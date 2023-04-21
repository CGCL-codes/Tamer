    public void onPacketSended(IOBuffer readBuf, IOBuffer writeBuf) {
        logger.debug("DEBUG ENTER");
        switch(state) {
            case WRITE_INIT:
                state = State.READ_AUTH;
                readPacket(readBuf);
                break;
            case WRITE_RESULT:
                state = State.READ_COMMOND;
                readPacket(readBuf);
                break;
            case CLOSE:
            default:
                session.setNextState(MyBridgeSession.STATE_CLOSE);
        }
    }
