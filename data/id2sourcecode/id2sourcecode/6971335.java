    public String generateChannel() {
        Random generator = new Random(Calendar.getInstance().getTimeInMillis());
        String userId = "clientId-" + generator.nextInt();
        ChannelService channelService = ChannelServiceFactory.getChannelService();
        String token = channelService.createChannel(userId);
        this.connectedClients.add(userId);
        return token;
    }
