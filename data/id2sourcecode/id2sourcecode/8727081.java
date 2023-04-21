    @Override
    public void execute() {
        ClientCommunication client = GameContext.getClientCommunication();
        Set<ClientChannel> channels = client.getChannelConteiner().getChannelsOfType(this.getChannelType());
        if (channels == null) throw new NullPointerException("Si pas� esto es porque nos esta arruinando al concurrencia.");
        for (ClientChannel cc : channels) {
            try {
                client.sendEvent(this.getMessage(), cc);
            } catch (SendMessageException e) {
                LOGGER.log(Level.WARNING, "No se pudo enviar el mensaje: {0}", e);
                throw new RuntimeException(e);
            }
        }
    }
