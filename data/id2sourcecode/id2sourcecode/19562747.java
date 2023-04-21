    @Override
    public void onKick(KickEvent<KEllyBot> event) throws Exception {
        super.onKick(event);
        updateWho(event.getChannel());
        queueMessage(new Message(nc, event.getRecipient().getNick() + " was kicked by " + event.getSource().getNick() + (event.getReason() != null ? " [" + event.getReason() + "]" : ""), event.getChannel().getName(), event.getChannel().getName(), Message.CONSOLE));
    }
