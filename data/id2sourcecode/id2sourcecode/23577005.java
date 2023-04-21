    protected void onConnect() {
        System.out.println("connesso a: " + server.getServer());
        List<String> channels = server.getChannels();
        for (String channel : channels) this.joinChannel(channel);
    }
