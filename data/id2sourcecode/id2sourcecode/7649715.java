            public void run() {
                RPCMessage message_ = (RPCMessage) e.getMessage();
                Header hdr = message_.getHeader(name);
                RPCMessage rsp;
                try {
                    rsp = messageReceived(message_);
                    switch(hdr.getType()) {
                        case Header.REQ:
                            if (rsp != null) e.getChannel().write(rsp);
                            break;
                        case Header.RSP:
                            break;
                        default:
                            break;
                    }
                } catch (Exception e1) {
                    log.error(e1);
                }
            }
