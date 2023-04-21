    @Override
    public AKSpeak applyNoise(AKSpeak message) {
        byte[] data = message.getContent();
        for (int i = 0; i < data.length; ++i) {
            for (int j = 0; j < BITS; ++j) {
                if (random.nextDouble() < p) {
                    data[i] = (byte) (data[i] ^ (1 << j));
                }
            }
        }
        return new AKSpeak(message.getAgentID(), message.getTime(), message.getChannel(), data);
    }
