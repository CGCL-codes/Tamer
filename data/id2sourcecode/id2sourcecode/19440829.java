    public void connect() throws SMAPException {
        if (this.session.isOpen() == false) {
            try {
                this.session.open();
            } catch (SMAPException se) {
                throw new SMAPException("unable to open connection to " + this.server + ":" + this.port, se);
            }
        }
        if (this.channel != null) {
            try {
                close();
            } catch (Exception e) {
            }
        }
        try {
            this.channel = this.session.getChannel();
        } catch (SMAPException se) {
            throw new SMAPException("unable to smap " + this.server + ":" + this.port, se);
        }
        if (this.bufferSize == 0) {
            this.bufferSize = getWindowSize();
        } else {
            try {
                setWindowSize(this.bufferSize);
            } catch (SMAPException se) {
            }
        }
        return;
    }
