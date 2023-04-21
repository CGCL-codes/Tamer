        public void connect() {
            try {
                ChannelAppletServer.getInstance().registerApplet(callParameters.getChannelApplet());
                callQueue.put(this);
            } catch (InterruptedException e) {
                throw new AsteriskPbxRuntimeException("could not queue call for origination", e);
            }
        }
