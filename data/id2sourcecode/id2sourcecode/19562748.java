    @Override
    public void onTopic(TopicEvent<KEllyBot> event) throws Exception {
        super.onTopic(event);
        nc.updateTopic(event.getChannel().getName());
        if (event.getDate() < System.currentTimeMillis()) {
            queueMessage(new Message(nc, event.getTopic(), event.getChannel().getName(), event.getChannel().getName(), Message.CONSOLE));
            queueMessage(new Message(nc, "Topic set by: " + event.getUser().getNick(), event.getChannel().getName(), event.getChannel().getName(), Message.CONSOLE));
        } else {
            queueMessage(new Message(nc, event.getUser().getNick() + " changed the topic.", event.getChannel().getName(), event.getChannel().getName(), Message.CONSOLE));
        }
    }
