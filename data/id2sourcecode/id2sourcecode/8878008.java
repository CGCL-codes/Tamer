    public void down(Event evt) {
        if (evt.getType() == Event.MSG) {
            Message msg = (Message) evt.getArg();
            msg.putHeader(ID, hdr);
        }
        mux.getChannel().down(evt);
    }
