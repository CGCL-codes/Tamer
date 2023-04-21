    public Object downcall(Event evt) {
        if (evt.getType() == Event.MSG) {
            Message msg = (Message) evt.getArg();
            msg.putHeader(ID, hdr);
        }
        return mux.getChannel().downcall(evt);
    }
