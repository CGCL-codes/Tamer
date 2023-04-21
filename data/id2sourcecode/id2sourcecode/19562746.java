    @Override
    public void onPart(PartEvent<KEllyBot> event) throws Exception {
        super.onPart(event);
        updateWho(event.getChannel());
        queueMessage(new Message(nc, event.getUser().getNick() + " has parted.", event.getChannel().getName(), event.getChannel().getName(), Message.CONSOLE));
    }
