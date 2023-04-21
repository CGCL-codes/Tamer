    private boolean updateDmxOutputs() {
        boolean changes = false;
        for (int i = 0; i < context.getShow().getNumberOfDimmers(); i++) {
            int newLevel = 0;
            int channelIndex = context.getShow().getDimmers().get(i).getChannelId();
            if (channelIndex != -1) {
                newLevel = channelDmxOutputs[channelIndex];
            }
            if (dimmerDmxOutputs[i] > newLevel) {
                newLevel = dimmerDmxOutputs[i];
            }
            if (dmxOutputs[i] != newLevel) {
                dmxOutputs[i] = newLevel;
                changes = true;
            }
        }
        return changes;
    }
