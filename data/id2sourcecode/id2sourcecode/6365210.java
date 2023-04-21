    private org.smapcore.smap.transport.SMAPChannel getChannel(boolean retry) throws SMAPException {
        Channel channel;
        if (isOpen() == false) {
            open();
        }
        System.out.println(this.session.toString());
        try {
            channel = this.session.startChannel(BEEPProfile.URI);
        } catch (BEEPError be) {
            if (be.getCode() == 550) {
                throw new SMAPException(this.server + ":" + this.port + " does not support " + BEEPProfile.URI, be);
            } else {
                if (retry == true) {
                    throw new SMAPException("lost connection to " + this.server + ":" + this.port, be);
                } else {
                    try {
                        close();
                    } catch (SMAPException se) {
                    }
                    try {
                        open();
                    } catch (SMAPException se) {
                        throw new SMAPException("cannot connect to " + this.server + ":" + this.port, se);
                    }
                    return (getChannel(true));
                }
            }
        } catch (BEEPException be) {
            throw new SMAPException("cannot connect to " + this.server + ":" + this.port, be);
        }
        return (new SMAPChannel(channel));
    }
