        public void onTopicCommand(TopicCommand command) {
            String senderName = command.getSender().getNickname();
            String text = WebIRC.eventMessages.topicChanged(senderName, command.getTopic());
            MessageCommand msg = new MessageCommand(text, MessageCommand.TYPE_TOPIC);
            messagePanel.addMessage(command.getChannel(), msg);
        }
