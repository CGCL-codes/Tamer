    public boolean channelCheck() {
        MessageBrokerController controller = MessageBrokerController.getInstance();
        Set<String> channels = controller.getChannelSet();
        Iterator<String> iterator = channels.iterator();
        while (iterator.hasNext()) {
            if (iterator.next().equals(command[1])) {
                return true;
            }
        }
        return false;
    }
