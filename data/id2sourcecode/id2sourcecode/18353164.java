    public void sendMessage(JID toAddress, FileshareComponentBuilder builder) {
        MessageBuilder mb = new MessageBuilder();
        mb.setToAddress(toAddress);
        mb.setType("chat");
        builder.setHashedID(status.getHashedID());
        try {
            mb.addExtension(builder.build());
            connBean.send(mb.build());
        } catch (InstantiationException e) {
            System.err.println("*EE* Could not send message: " + e);
            return;
        }
    }
