        public void receiveMSG(MessageMSG message) {
            try {
                listener.receiveMSG(message);
            } catch (BEEPError e) {
                try {
                    message.sendERR(e);
                } catch (BEEPException e2) {
                    log.error("Error sending ERR", e2);
                }
            } catch (AbortChannelException e) {
                try {
                    message.getChannel().close();
                } catch (BEEPException e2) {
                    log.error("Error closing channel", e2);
                }
            }
        }
