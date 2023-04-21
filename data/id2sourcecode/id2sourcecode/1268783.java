        public void run() {
            while (true) {
                PendingMsg pm = outBox.get();
                GenericMessage msg = pm.getMessage();
                AID receiverID = pm.getReceiver();
                Channel ch = pm.getChannel();
                try {
                    ch.deliverNow(msg, receiverID);
                } catch (Throwable t) {
                    myLogger.log(Logger.WARNING, "MessageManager cannot deliver message " + stringify(msg) + " to agent " + receiverID.getName(), t);
                    ch.notifyFailureToSender(msg, receiverID, new InternalError(ACLMessage.AMS_FAILURE_UNEXPECTED_ERROR + ": " + t));
                }
                servedCnt++;
                outBox.handleServed(receiverID);
            }
        }
