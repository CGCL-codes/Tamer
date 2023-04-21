    @Override
    public AKSpeak applyNoise(AKSpeak message) {
        if (random.nextDouble() >= p) {
            return message;
        }
        return new AKSpeak(message.getAgentID(), message.getTime(), message.getChannel(), new byte[0]);
    }
