        public void receiveMSG(MessageMSG message) {
            log.error("No handler registered to process MSG received on " + "channel " + message.getChannel().getNumber());
            try {
                message.sendERR(BEEPError.CODE_REQUESTED_ACTION_ABORTED, "No MSG handler registered");
            } catch (BEEPException e) {
                log.error("Error sending ERR", e);
            }
        }
