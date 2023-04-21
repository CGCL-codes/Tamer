    public void exec(Command command) throws IOException {
        if (command.getChannel() == FlapConstants.FLAP_CHANNEL_DISCONNECT) {
            TLV tlvServer = command.getTLV(TLVConstants.TLV_TYPE_SERVER);
            TLV tlvCookie = command.getTLV(TLVConstants.TLV_TYPE_COOKIE);
            TLV tlvError = command.getTLV(TLVConstants.TLV_TYPE_ERROR_CODE);
            if (tlvServer != null && tlvCookie != null) {
                int port = 5190;
                String server = null;
                String serverCookie = tlvServer.getStringValue();
                int pos = serverCookie.indexOf(':');
                if (pos != -1) {
                    server = serverCookie.substring(0, pos);
                    port = Integer.parseInt(serverCookie.substring(pos + 1));
                } else {
                    server = serverCookie;
                }
                listeners.eventHappened(new IMEvent(this, OscarEventName.reconnect, server, port, tlvCookie.getValue()));
            } else if (tlvError != null) {
                log.severe("Authentication error - error code=0x" + ByteUtils.toHexString(tlvError.getValue()));
            }
        }
    }
