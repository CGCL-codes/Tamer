    public final boolean processAtServer() {
        int count = readInt();
        Transaction ta = transaction();
        for (int i = 0; i < count; i++) {
            StatefulBuffer writer = _payLoad.readYapBytes();
            int messageId = writer.readInt();
            Msg message = Msg.getMessage(messageId);
            Msg clonedMessage = message.publicClone();
            clonedMessage.setMessageDispatcher(messageDispatcher());
            clonedMessage.setTransaction(ta);
            if (clonedMessage instanceof MsgD) {
                MsgD msgd = (MsgD) clonedMessage;
                msgd.payLoad(writer);
                if (msgd.payLoad() != null) {
                    msgd.payLoad().incrementOffset(Const4.INT_LENGTH);
                    Transaction t = checkParentTransaction(ta, msgd.payLoad());
                    msgd.setTransaction(t);
                    ((ServerSideMessage) msgd).processAtServer();
                }
            } else {
                ((ServerSideMessage) clonedMessage).processAtServer();
            }
        }
        return true;
    }
