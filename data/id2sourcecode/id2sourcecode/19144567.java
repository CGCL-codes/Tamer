    public AceMessageInterface waitInputStreamMessage() {
        Thread thr = Thread.currentThread();
        if ((thr instanceof AceThread) == false) {
            writeErrorMessage("This method is not being called from an object which is a sub-class of type AceThread");
            return null;
        }
        AceThread cthread = (AceThread) thr;
        while (true) {
            AceMessageInterface msg_received = cthread.waitMessage();
            if ((msg_received instanceof AceInputSocketStreamMessage) == true) {
                if (((AceInputSocketStreamMessage) msg_received).getInputSocketStream() == this) {
                    return msg_received;
                }
            } else if ((msg_received instanceof AceSignalMessage) == true) {
                return msg_received;
            }
        }
    }
