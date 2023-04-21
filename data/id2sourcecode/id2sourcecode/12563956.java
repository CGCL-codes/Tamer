    public <T extends Event> T postEvent(T event, DistributorMessage message) {
        for (Device d : distributor2devices.get(message.getChannel())) d.receiveEvent(event);
        return null;
    }
