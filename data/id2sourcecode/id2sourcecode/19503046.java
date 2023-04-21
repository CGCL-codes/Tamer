    public int available() {
        int available = 0;
        if (msgdata != null) {
            available = msgdata.length - currentPos;
            if (log.isDebugEnabled() && (available > 0)) {
                log.debug(String.valueOf(available) + " bytes of channel data available");
            }
            available = (available >= 0) ? available : 0;
        }
        if (available == 0) {
            try {
                if (type != null) {
                    SshMsgChannelExtendedData msg = (SshMsgChannelExtendedData) messageStore.peekMessage(filter);
                    available = msg.getChannelData().length;
                } else {
                    SshMsgChannelData msg = (SshMsgChannelData) messageStore.peekMessage(filter);
                    available = msg.getChannelData().length;
                }
                if (log.isDebugEnabled()) {
                    log.debug(String.valueOf(available) + " bytes of channel data available");
                }
            } catch (MessageStoreEOFException mse) {
                log.debug("No bytes available since the MessageStore is EOF");
                available = -1;
            } catch (MessageNotAvailableException mna) {
                available = 0;
            } catch (InterruptedException ex) {
                log.info("peekMessage was interrupted, no data available!");
                available = 0;
            }
        }
        return available;
    }
